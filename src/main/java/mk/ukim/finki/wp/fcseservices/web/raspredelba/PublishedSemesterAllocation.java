package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectAllocationsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;


@Controller
public class PublishedSemesterAllocation {


    private final TeacherSubjectAllocationsService teacherSubjectAllocationService;
    private final JoinedSubjectService joinedSubjectService;
    private final ProfessorService professorService;

    @Value("${system.encryption.key}")
    private String key;

    public PublishedSemesterAllocation(TeacherSubjectAllocationsService teacherSubjectAllocationService, JoinedSubjectService joinedSubjectService, ProfessorService professorService) {
        this.teacherSubjectAllocationService = teacherSubjectAllocationService;
        this.joinedSubjectService = joinedSubjectService;
        this.professorService = professorService;
    }

    @GetMapping("/allocation/{allocationId}")
    public String semesterAllocation(@PathVariable String allocationId,
                                     @RequestParam(required = false) String subject,
                                     @RequestParam(required = false) String professor,
                                     Model model) throws Exception {
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String semesterCode = new String(cipher.doFinal(HexFormat.of().parseHex(new StringBuffer(allocationId))));
        System.out.println(semesterCode);


        Specification<TeacherSubjectAllocations> filter = Specification
                .where(filterEquals(TeacherSubjectAllocations.class, "semester.code", semesterCode))
                .and(filterEquals(TeacherSubjectAllocations.class, "subject.abbreviation", subject))
                .and(filterEquals(TeacherSubjectAllocations.class, "professor.id", professor));

        Page<TeacherSubjectAllocations> page = teacherSubjectAllocationService.findAll(filter, 1, 100_000);
        Map<JoinedSubject, List<TeacherSubjectAllocations>> subjectAllocations = new TreeMap<>(Comparator.comparing(JoinedSubject::getName));
        subjectAllocations.putAll(page.getContent()
                .stream()
                .collect(groupingBy(TeacherSubjectAllocations::getSubject))
        );

        model.addAttribute("subjectAllocations", subjectAllocations);
        model.addAttribute("semesterCode", semesterCode);
        model.addAttribute("allocationId", allocationId);
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("subjects", joinedSubjectService.getAllJoinedSubjects());
        return "raspredelba/allocation/preview";
    }

    @GetMapping("/admin/allocation/generate-id/{semesterCode}")
    @ResponseBody
    public String getAllocationId(@PathVariable String semesterCode) throws Exception {
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(semesterCode.getBytes());
        System.err.println(new String(encrypted));
        return String.format("/allocation/%s", HexFormat.of().formatHex(encrypted));

    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(32);
        return keygenerator.generateKey();
    }

    public static IvParameterSpec generateIv() {
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return new IvParameterSpec(initializationVector);
    }


}

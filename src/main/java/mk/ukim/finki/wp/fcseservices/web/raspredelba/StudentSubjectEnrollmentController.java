package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.dto.SubjectEnrollmentDTO;
import mk.ukim.finki.wp.fcseservices.model.enrollments.StudentSubjectEnrollment;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.StudentService;
import mk.ukim.finki.wp.fcseservices.service.StudentSubjectEnrollmentService;
import mk.ukim.finki.wp.fcseservices.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterContainsText;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;
import static org.springframework.data.jpa.domain.Specification.where;

@Controller
@RequestMapping("/admin/enroll/student-subject")
public class StudentSubjectEnrollmentController {

    private final StudentSubjectEnrollmentService studentSubjectEnrollmentService;
    private final SemesterManagementService semesterManagementService;
    private final SubjectService subjectService;
    private final StudentService studentService;

    private final ImportRepository importRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;

    public StudentSubjectEnrollmentController(StudentSubjectEnrollmentService studentSubjectEnrollmentService,
                                              SemesterManagementService semesterManagementService, SubjectService subjectService, StudentService studentService, ImportRepository importRepository,
                                              JoinedSubjectRepository joinedSubjectRepository) {
        this.studentSubjectEnrollmentService = studentSubjectEnrollmentService;
        this.semesterManagementService = semesterManagementService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.importRepository = importRepository;
        this.joinedSubjectRepository = joinedSubjectRepository;
    }

    @GetMapping
    public String getAllStudentSubjectEnrollments(Model model,
                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "20") Integer results,
                                                  @RequestParam(required = false) String studentId,
                                                  @RequestParam(required = false) String subject,
                                                  @RequestParam(required = false) String groupName,
                                                  @RequestParam(required = false) String semesterCode,
                                                  @RequestParam(required = false) String joinedSubject) {

        Page<StudentSubjectEnrollment> studentSubjectEnrollmentsPage;

        Specification<StudentSubjectEnrollment> spec =
                where(filterEquals(StudentSubjectEnrollment.class, "student.index", studentId))
                        .and(filterEquals(StudentSubjectEnrollment.class, "subject.id", subject))
                        .and(filterContainsText(StudentSubjectEnrollment.class, "groupName", groupName))
                        .and(filterEquals(StudentSubjectEnrollment.class, "semester.code", semesterCode))
                        .and(filterEquals(StudentSubjectEnrollment.class, "joinedSubject.abbreviation", joinedSubject));


        studentSubjectEnrollmentsPage = studentSubjectEnrollmentService.list(spec, pageNum, results);

        model.addAttribute("page", studentSubjectEnrollmentsPage);

        model.addAttribute("semesters", semesterManagementService.getAllSemesters());
        model.addAttribute("subjects", joinedSubjectRepository.findAll());

        return "raspredelba/student-subject/listStudentSubjectEnrollments";
    }


    @GetMapping(params = "export")
    public void export(Model model,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "2000") Integer results,
                       @RequestParam(required = false) String studentId,
                       @RequestParam(required = false) String subject,
                       @RequestParam(required = false) String groupName,
                       @RequestParam(required = false) String semesterCode,
                       @RequestParam(required = false) String joinedSubject,
                       HttpServletResponse response) {

        Page<StudentSubjectEnrollment> page;

        Specification<StudentSubjectEnrollment> spec =
                where(filterEquals(StudentSubjectEnrollment.class, "student.index", studentId))
                        .and(filterEquals(StudentSubjectEnrollment.class, "subject.id", subject))
                        .and(filterContainsText(StudentSubjectEnrollment.class, "groupName", groupName))
                        .and(filterEquals(StudentSubjectEnrollment.class, "semester.code", semesterCode))
                        .and(filterEquals(StudentSubjectEnrollment.class, "joinedSubject.abbreviation", joinedSubject));


        page = studentSubjectEnrollmentService.list(spec, pageNum, results);

        doExport(response,
                page.getContent().stream().map(it -> new SubjectEnrollmentDTO(
                        it.getStudent().getIndex(),
                        it.getSubject().getId(),
                        it.getNumEnrollments().intValue(),
                        it.getProfessorId(),
                        it.getJoinedSubject() != null ? it.getJoinedSubject().displayName() : null,
                        it.getGroupName()
                )).collect(Collectors.toList()));
    }

    @PostMapping("/import")
    public void importStudentSubject(@RequestParam String semester, @RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<SubjectEnrollmentDTO> enrollments = importRepository.readTypeList(file, SubjectEnrollmentDTO.class);

        List<SubjectEnrollmentDTO> invalidEnrollments = studentSubjectEnrollmentService.importStudentSubjects(enrollments, semester);

        String fileName = "invalid_enrollments_" + semester + ".tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(SubjectEnrollmentDTO.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/delete")
    public String deleteStudentSubjectBySemester(@RequestParam String semester) {
        studentSubjectEnrollmentService.deleteBySemester(semester);
        return "redirect:/admin/enroll/student-subject";
    }

    @PostMapping("/allocate-groups-subjects")
    public String allocateGroupsAndSubjects(@RequestParam String semester) {
        studentSubjectEnrollmentService.allocateGroupsAndSubjects(semester);
        return "redirect:/admin/enroll/student-subject";
    }

    @PostMapping("/allocate-professors")
    public String allocateProfessors(@RequestParam String semester) {
        studentSubjectEnrollmentService.allocateEnrollmentProfessors(semester);
        return "redirect:/admin/enroll/student-subject";
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<SubjectEnrollmentDTO> example = new ArrayList<>();
        example.add(new SubjectEnrollmentDTO("111111", "F23L2W012", 1, null, null, null));
        example.add(new SubjectEnrollmentDTO("111112", "F23L2W112", 5, null, null, null));
        example.add(new SubjectEnrollmentDTO("111113", "F23L2W912", 2, null, null, null));


        doExport(response, example);
    }

    public void doExport(HttpServletResponse response, List<SubjectEnrollmentDTO> data) {
        String fileName = "enrollments_example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(SubjectEnrollmentDTO.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

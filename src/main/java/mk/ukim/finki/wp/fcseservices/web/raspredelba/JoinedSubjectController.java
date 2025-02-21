package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.*;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectDTO;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.SubjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/joined-subjects")
public class JoinedSubjectController {

    private final JoinedSubjectService joinedSubjectService;
    private final SubjectService subjectService;

    private final SemesterManagementService semesterManagementService;

    private final String subjectDetailsPath;

    private final ImportRepository importRepository;

    public JoinedSubjectController(JoinedSubjectService joinedSubjectService,
                                   SubjectService subjectService,
                                   SemesterManagementService semesterManagementService, @Value("${finki.akreditacii.subject-url-prefix}") String subjectDetailsPath,
                                   ImportRepository importRepository) {
        this.joinedSubjectService = joinedSubjectService;
        this.subjectService = subjectService;
        this.semesterManagementService = semesterManagementService;
        this.subjectDetailsPath = subjectDetailsPath;
        this.importRepository = importRepository;
    }

    @ModelAttribute(name = "subjectDetailsPath")
    public String subjectDetailsPath() {
        return subjectDetailsPath;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String mainSubject,
                       @RequestParam(required = false) SemesterType semesterType,
                       @RequestParam(required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                       LocalDate modifiedAfter,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results) {

        Page<JoinedSubject> page = joinedSubjectService.filter(name,
                mainSubject, semesterType, modifiedAfter,
                pageNum, results);

        model.addAttribute("page", page);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("semesters", semesterManagementService.getAllSemesters());

        return "raspredelba/joined-subjects/list";
    }

    @GetMapping("/add")
    public String showJoinedSubjectForm(Model model) {
        model.addAttribute("joinedSubject", new JoinedSubject());
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "raspredelba/joined-subjects/addForm";
    }

    @GetMapping("/edit/{abbreviation}")
    public String showEditJoinedSubjectForm(@PathVariable String abbreviation, Model model) {
        JoinedSubject joinedSubject = joinedSubjectService.getByAbbreviation(abbreviation);
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        model.addAttribute("joinedSubject", joinedSubject);
        return "raspredelba/joined-subjects/editForm";
    }

    @GetMapping("/edit-id/{abbreviation}")
    public String showEditJoinedSubjectIdForm(@PathVariable String abbreviation, Model model) {
        JoinedSubject joinedSubject = joinedSubjectService.getByAbbreviation(abbreviation);
        model.addAttribute("joinedSubject", joinedSubject);
        return "raspredelba/joined-subjects/editAbbreviationForm";
    }

    @PostMapping("/save-id")
    public String saveUpdatedJoinedSubjectId(@ModelAttribute("joinedSubject") JoinedSubject joinedSubject,
                                             @RequestParam("newId") String newId,
                                             Model model) {
        try {
            joinedSubjectService.updateAbbreviation(joinedSubject.getAbbreviation(), newId);
            return "redirect:/admin/joined-subjects";
        } catch (IllegalArgumentException e) {
            JoinedSubject originalJoinedSubject = joinedSubjectService.getByAbbreviation(joinedSubject.getAbbreviation());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("joinedSubject", originalJoinedSubject);
            return "raspredelba/joined-subjects/editAbbreviationForm";
        }
    }


    @PostMapping("/save")
    public String saveJoinedSubject(@ModelAttribute("joinedSubject") JoinedSubject joinedSubject,
                                    @RequestParam("mainSubjectId") String mainSubjectId) {
        Subject mainSubject = subjectService.getSubjectById(mainSubjectId);
        joinedSubject.setMainSubject(mainSubject);
        joinedSubjectService.save(joinedSubject);
        return "redirect:/admin/joined-subjects";
    }

    @GetMapping("/delete/{abbreviation}")
    public String deleteJoinedSubject(@PathVariable String abbreviation) {
        joinedSubjectService.delete(abbreviation);
        return "redirect:/admin/joined-subjects";
    }

    @GetMapping("/display/{abbreviation}")
    public String displayJoinedSubject(@PathVariable String abbreviation, Model model) {
        JoinedSubject joinedSubject = joinedSubjectService.getByAbbreviation(abbreviation);
        model.addAttribute("joinedSubject", joinedSubject);
        return "raspredelba/joined-subjects/display";
    }

    @PostMapping("restore/{abbreviation}")
    public String restore(@PathVariable String abbreviation,
                          @RequestParam String name,
                          @RequestParam String codes,
                          @RequestParam SemesterType semesterType,
                          @RequestParam String mainSubjectId,
                          @RequestParam Integer weeklyLecturesClasses,
                          @RequestParam Integer weeklyAuditoriumClasses,
                          @RequestParam Integer weeklyLabClasses
    ) {

        JoinedSubject subject = joinedSubjectService.getByAbbreviation(abbreviation);
        subject.setName(name);
        subject.setCodes(codes);
        subject.setSemesterType(semesterType);
        subject.setMainSubject(this.subjectService.getSubjectById(mainSubjectId));
        subject.setWeeklyLecturesClasses(weeklyLecturesClasses);
        subject.setWeeklyAuditoriumClasses(weeklyAuditoriumClasses);
        subject.setWeeklyLabClasses(weeklyLabClasses);
        joinedSubjectService.save(subject);
        return "redirect:/admin/joined-subjects/display/" + abbreviation;
    }

    @GetMapping(params = "export")
    public void export(@RequestParam(required = false) String name,
                @RequestParam(required = false) String mainSubject,
                @RequestParam(required = false) SemesterType semesterType,
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate modifiedAfter,
                @RequestParam(defaultValue = "1") Integer pageNum,
                @RequestParam(defaultValue = "2000") Integer results,
                HttpServletResponse response)

    {

        Page<JoinedSubject> page = joinedSubjectService.filter(name,
                mainSubject, semesterType, modifiedAfter,
                pageNum, results);

        doExport(response, page.getContent().stream().map(it -> new JoinedSubjectDTO(
                it.getAbbreviation(),
                it.getName(),
                it.getCodes(),
                it.getSemesterType().name(),
                it.getMainSubject().getId(),  //code
                it.getWeeklyLecturesClasses(),
                it.getWeeklyAuditoriumClasses(),
                it.getWeeklyLabClasses(),
                it.getCycle().name(),
                it.getValidationMessage()
        )).collect(Collectors.toList()));
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<JoinedSubjectDTO> example = new ArrayList<>();
        example.add(new JoinedSubjectDTO("ANM", "Администрација на мрежи", "F23L3S059;F18L3S059", "SUMMER", "F23L3S059", 3, 2, 0, "UNDERGRADUATE", ""));
        example.add(new JoinedSubjectDTO("ANS", "Администрација на системи", "F23L3W060;CSEW504;F18L3W060", "WINTER", "F23L3W060",3, 2, 0, "UNDERGRADUATE",""));
        example.add(new JoinedSubjectDTO("APS", "Алгоритми и податочни структури", "CSEW301;F18L2W001;F23L2W001", "WINTER", "F23L2W001",3, 2, 0, "UNDERGRADUATE", ""));

        doExport(response, example);
    }

    @PostMapping("/import")
    public void importJoinedSubjects(@RequestParam String semester,
                                     @RequestParam("file") MultipartFile file,
                                     HttpServletResponse response) {

        List<JoinedSubjectDTO> subjects = importRepository.readPreferences(file, JoinedSubjectDTO.class);

        List<JoinedSubjectDTO> invalidSubjects = joinedSubjectService.importJoinedSubjects(subjects, semester);

        String fileName = "invalid_preferences_" + semester + ".tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writePreferences(JoinedSubjectDTO.class, invalidSubjects, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doExport(HttpServletResponse response, List<JoinedSubjectDTO> data) {
        String fileName = "joined_subjects.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(JoinedSubjectDTO.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

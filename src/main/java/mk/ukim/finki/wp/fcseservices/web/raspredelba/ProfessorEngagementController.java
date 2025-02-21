package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.config.FacultyUserDetails;
import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.*;
import mk.ukim.finki.wp.fcseservices.model.base.ClassType;
import mk.ukim.finki.wp.fcseservices.model.base.Language;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/engagement")
public class ProfessorEngagementController {

    private final ProfessorEngagementService service;
    private final ProfessorEngagementInitializationService initService;
    private final JoinedSubjectService subjectService;
    private final SemesterManagementService semesterService;
    private final ProfessorService professorService;
    private final ImportRepository importRepository;

    @GetMapping("/all")
    public String all(@RequestParam(required = false) String professor,
                      @RequestParam(required = false) String semester,
                      @RequestParam(required = false) String subject,
                      @RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "20") Integer results,
                      Model model) {
        return engagementsByProfessor(professor, semester, subject, pageNum, results, model);
    }

    @GetMapping("/{professor}")
    public String engagementsByProfessor(@PathVariable(required = false) String professor,
                                         @RequestParam(required = false) String semester,
                                         @RequestParam(required = false) String subject,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "20") Integer results,
                                         Model model) {
        Page<ProfessorEngagement> page = service.findPage(professor, semester, subject, pageNum, results);

        model.addAttribute("subjects", subjectService.getAllJoinedSubjects());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("page", page);

        return "raspredelba/engagement/list";
    }

    @GetMapping(value = {"/all", "/{professorId}"}, params = "export")
    public void export(@PathVariable(required = false) String professorId,
                       @RequestParam(required = false) String professor,
                       @RequestParam(required = false) String semester,
                       @RequestParam(required = false) String subject,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "1000") Integer results,
                       Model model,
                       HttpServletResponse response) {
        String prof = Optional.ofNullable(professorId).orElse(professor);
        engagementsByProfessor(prof, semester, subject, pageNum, results, model);
        Page<ProfessorEngagement> page = (Page<ProfessorEngagement>) model.getAttribute("page");
        List<ProfessorEngagement> data = page.getContent();

        String fileName = String.format("engagement-%s-%s.tsv", Optional.ofNullable(prof).orElse("all"),
                Optional.ofNullable(semester).orElse("all"));
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(ProfessorEngagement.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/my")
    public String myEngagements(@AuthenticationPrincipal FacultyUserDetails principal,
                                @RequestParam(required = false) String semester,
                                RedirectAttributes redirectAttributes) {
        if (semester != null) {
            redirectAttributes.addAttribute("semester", semester);
        }
        return "redirect:/engagement/" + principal.getUsername();
    }

    @GetMapping("/add")
    public String getAddDeclarationPage(@RequestParam String returnTo, Model model) {
        model.addAttribute("subjects", subjectService.getAllJoinedSubjects());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("classes", ClassType.values());
        model.addAttribute("languages", Language.values());
        model.addAttribute("returnTo", returnTo);
        return "raspredelba/engagement/form";
    }

    @GetMapping("/edit/{id}")
    public String getEditDeclarationPage(@PathVariable(name = "id") String id,
                                         @RequestParam String returnTo,
                                         Model model) {
        model.addAttribute("entity", service.findById(id));
        model.addAttribute("subjects", subjectService.getAllJoinedSubjects());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("classes", ClassType.values());
        model.addAttribute("languages", Language.values());
        model.addAttribute("returnTo", returnTo);
        return "raspredelba/engagement/form";
    }


    @PostMapping("/save/")
    public String addDeclaration(@RequestParam(name = "subject") String subjectId,
                                 @RequestParam(name = "classType") ClassType classType,
                                 @RequestParam(name = "numberOfClasses") Float numberOfClasses,
                                 @RequestParam(name = "otherTeacher", required = false) boolean otherTeacher,
                                 @RequestParam(name = "language") Language language,
                                 @RequestParam(name = "numberOfStudents") Short numberOfStudents,
                                 @RequestParam(name = "consultative", required = false) boolean consultative,
                                 @RequestParam(name = "note") String note,
                                 @RequestParam(name = "semester") String semesterId,
                                 @RequestParam String returnTo,
                                 @AuthenticationPrincipal FacultyUserDetails principal) throws Exception {
        service.addNew(
                principal.getProfessor(),
                subjectId,
                classType,
                numberOfClasses,
                otherTeacher,
                language,
                numberOfStudents,
                consultative,
                note,
                semesterId);
        return "redirect:" + returnTo;
    }

    @PostMapping("/save/{id}")
    public String addDeclaration(@PathVariable(name = "id") String declarationId,
                                 @RequestParam(name = "numberOfClasses") Float numberOfClasses,
                                 @RequestParam(name = "otherTeacher", required = false) boolean otherTeacher,
                                 @RequestParam(name = "language") Language language,
                                 @RequestParam(name = "numberOfStudents") Short numberOfStudents,
                                 @RequestParam(name = "consultative", required = false) boolean consultative,
                                 @RequestParam(name = "note") String note,
                                 @RequestParam(name = "semester") String semesterId,
                                 @RequestParam String returnTo) {
        service.update(declarationId, numberOfClasses, otherTeacher, language, numberOfStudents,
                consultative, note, semesterId);
        return "redirect:" + returnTo;
    }


    @PostMapping("/delete/{id}")
    public String deleteDeclaration(@PathVariable(name = "id") String id) throws Exception {
        service.delete(id);
        return "redirect:/engagement/my";
    }

    @PostMapping("/init")
    public String initialize(@RequestParam String semester) throws Exception {
        initService.initSemester(semester);
        return "redirect:/engagement/my?semester=" + semester;
    }

}

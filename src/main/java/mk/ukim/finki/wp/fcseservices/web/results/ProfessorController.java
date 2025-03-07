package mk.ukim.finki.wp.fcseservices.web.results;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import mk.ukim.finki.wp.fcseservices.config.FacultyUserDetails;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.service.CourseService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.ResultsService;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/professors")
public class ProfessorController {
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final ResultsService resultsService;
    private final YearExamSessionService yearExamSessionService;

    public ProfessorController(ProfessorService professorService, CourseService courseService, ResultsService resultsService, YearExamSessionService yearExamSessionService) {
        this.professorService = professorService;
        this.courseService = courseService;
        this.resultsService = resultsService;
        this.yearExamSessionService = yearExamSessionService;
    }

    @GetMapping("/my")
    public String myResults(@AuthenticationPrincipal FacultyUserDetails principal,
                            @RequestParam(required = false) String semester,
                            RedirectAttributes redirectAttributes) {
        if (semester != null) {
            redirectAttributes.addAttribute("semester", semester);
        }
        return "redirect:/professors/" + principal.getUsername();
    }

    @GetMapping("{professor}")
    public String getProfessorPage(@PathVariable String professor, Model model,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer size) {

        Page<Course> coursesByProfessorPage = this.courseService.findByProfessorIdPaginated(professor,pageNum, size);
        List<Results> results = this.resultsService.filterResultsProfessor(professor,coursesByProfessorPage.getContent());

        model.addAttribute("coursesPage", coursesByProfessorPage);
        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("professor", professor);
        model.addAttribute("results", results);

        return "results/professorPage";
    }

    @GetMapping("/{professor}/{courseId}/upload")
    public String getImportResults(@PathVariable String professor, @PathVariable Long courseId, Model model) {
        Course course1 = this.courseService.findById(courseId);
        Professor professor1 = this.professorService.findById(professor);
        List<YearExamSession> yearExamSessions = this.yearExamSessionService.findAll();
        model.addAttribute("course", course1);
        model.addAttribute("professor", professor1);
        model.addAttribute("sessions", yearExamSessions);
        return "results/professorUploadResult";
    }

    @PostMapping("/{professor}/{courseId}/upload")
    public String uploadResults(@PathVariable String professor,
                                @PathVariable Long courseId,
                                @RequestParam String sessionName,
                                @RequestParam @NotNull MultipartFile pdfFile,
                                @RequestParam String note,
                                @RequestParam String examType,
                                RedirectAttributes redirectAttributes) {
        try {
            this.resultsService.uploadResults(courseId,sessionName, pdfFile,note,examType,professor);
            redirectAttributes.addFlashAttribute("successMessage", "Results uploaded successfully.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload results. Please try again.");
        }
        return "redirect:/professors/{professor}";
    }
}

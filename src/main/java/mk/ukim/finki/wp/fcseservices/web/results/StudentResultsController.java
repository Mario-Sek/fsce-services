package mk.ukim.finki.wp.fcseservices.web.results;

import mk.ukim.finki.wp.fcseservices.config.FacultyUserDetails;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.results.ResultsTypes;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.ResultsService;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentResultsController {
    private final ResultsService resultsService;
    private final YearExamSessionService yearExamSessionService;
    private final ProfessorService professorService;

    public StudentResultsController(ResultsService resultsService, YearExamSessionService yearExamSessionService, ProfessorService professorService) {
        this.resultsService = resultsService;
        this.yearExamSessionService = yearExamSessionService;
        this.professorService = professorService;

    }

    @GetMapping("/my")
    public String myResults(@AuthenticationPrincipal FacultyUserDetails student,
                            @RequestParam(required = false) String semester,
                            RedirectAttributes redirectAttributes) {
        if (semester != null) {
            redirectAttributes.addAttribute("semester", semester);
        }
        return "redirect:/student/results/all/" + student.getUsername();
    }

    @GetMapping("/results/all/{studentIndex}")
    public String getFilteredStudentResultsPage(Model model, @PathVariable String studentIndex,
                                                @RequestParam(defaultValue = "") String session,
                                                @RequestParam(defaultValue = "") String professor,
                                                @RequestParam(defaultValue = "Any") String hasResult,
                                                @RequestParam(defaultValue = "") String examType,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {


        List<YearExamSession> sessions = yearExamSessionService.findAll();
        List<Professor> professors = professorService.findAll();

        Page<Results> result = resultsService.filterAndPaginateResultsStudent(studentIndex,professor,session, hasResult, examType, page, size);
        model.addAttribute("results", result);
        model.addAttribute("sessions", sessions);
        model.addAttribute("professors", professors);

        model.addAttribute("resultTypes", ResultsTypes.getAllTypesTranslated());
        model.addAttribute("studentIndex", studentIndex);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("hasResult", hasResult);
        model.addAttribute("size", size);
        model.addAttribute("selectedExamType",examType);


        return "results/studentResults";
    }

    @GetMapping("/results/download/{resultId}")
    public ResponseEntity<ByteArrayResource> downloadResults(@PathVariable Long resultId) {
        Optional<Results> optionalResults = resultsService.findById(resultId);

        if (optionalResults.isPresent()) {
            Results results = optionalResults.get();

            if (results.getPdfBytes() != null && results.getPdfBytes().length > 0) {
                ByteArrayResource resource = new ByteArrayResource(results.getPdfBytes());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "results.pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            }
        }

        return ResponseEntity.notFound().build();
    }

}

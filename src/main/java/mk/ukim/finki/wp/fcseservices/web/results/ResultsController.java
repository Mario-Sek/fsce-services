package mk.ukim.finki.wp.fcseservices.web.results;

import mk.ukim.finki.wp.fcseservices.model.results.HasResult;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.results.ResultsTypes;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.service.CourseService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.ResultsService;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.constraints.NotNull;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/results")
public class ResultsController {

    private final ResultsService resultsService;
    private final CourseService courseService;
    private final YearExamSessionService yearExamSessionService;
    private final ProfessorService professorService;

    public ResultsController(ResultsService resultsService, CourseService courseService, YearExamSessionService yearExamSessionService, ProfessorService professorService) {
        this.resultsService = resultsService;
        this.courseService = courseService;
        this.yearExamSessionService = yearExamSessionService;
        this.professorService = professorService;
    }


    @GetMapping("/all")
    public String getAllResultsPage(Model model,
                                    @RequestParam(defaultValue = "1") Long course,
                                    @RequestParam(defaultValue = "") String professor,
                                    @RequestParam(defaultValue = "") String semesterExamSession,
                                    @RequestParam(defaultValue = "Any") String hasResults,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "") String examType,
                                    @RequestParam(defaultValue = "10") Integer size) {

        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("semesterExamSessions", yearExamSessionService.findAll());
        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("resultTypes", ResultsTypes.getAllTypesTranslated());
        model.addAttribute("hasResultsOptions", HasResult.getAll());
        model.addAttribute("selectedResultType", examType);
        Course c = courseService.findById(course);
        List<Course> courses = new ArrayList<>();
        Optional.ofNullable(c).ifPresent(courses::add);
        model.addAttribute("page", resultsService
                .filterAndPaginateResultsAll(professor,semesterExamSession,courses,examType,hasResults, pageNum, size));
        return "results/resultsAll";
    }

    @GetMapping("/{id}/upload")
    public String uploadPage(@PathVariable Long id, Model model) {
        model.addAttribute("result", this.resultsService.findById(id));
        return "results/importResults";
    }

    @PostMapping("/{id}/upload")
    public String uploadPdf(@PathVariable Long id,
                            @RequestParam String professor,
                            @RequestParam @NotNull MultipartFile pdfFile,
                            RedirectAttributes redirectAttributes) {

        try {
            this.resultsService.savePdf(id, pdfFile);
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

        redirectAttributes.addAttribute("professor", professor);

        return "redirect:/admin/results/{professor}";

    }

    @GetMapping("/DownloadResultPdf/{resultId}")
    public HttpEntity<byte[]> DownloadPdfResult(@PathVariable Long resultId) {
        Results results = resultsService.findById(resultId).orElseThrow();
        byte[] pdfResultByte = results.getPdfBytes();
        String filename = "Results.pdf";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        httpHeaders.setContentLength(pdfResultByte.length);
        return new HttpEntity<>(pdfResultByte, httpHeaders);
    }


    @GetMapping("/initializeExamSession")
    public String initializeExamSessionPage(Model model,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "50") int size,
                                            @RequestParam(defaultValue = "sessionStart") String sortBy) {
        model.addAttribute("examSessions",
                this.yearExamSessionService.findAllWithPaginationAndSorting(page, size, sortBy).getContent());
        return "results/initializeExamSession";
    }


    @PostMapping("/initializeExamSession")
    public String createEmptyResults(@RequestParam String name) {
        this.resultsService.initializeForExamSession(name);

        return "redirect:/admin/results/all";
    }
    @GetMapping("/wall-of-shame")
        public String getWallOfShame(Model model){
            model.addAttribute("professorsNoResults",resultsService.findProfessorsWithNoResults());
            model.addAttribute("professorsLateResults",resultsService.getLateResults());
            return "results/wallOfShame";
        }

}

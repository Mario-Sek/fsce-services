package mk.ukim.finki.wp.fcseservices.web.dosie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.*;
import mk.ukim.finki.wp.fcseservices.model.exceptions.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final DisciplinaryRecordService reportService;
    private final StudentService studentService;
    private final JoinedSubjectService joinedSubjectService;
    private final ProfessorService professorService;
    private final DisciplinaryTypeService categoryService;
    private final DisciplinarySanctionService disciplinarySanctionService;
    private final MeetingService meetingService;
    public boolean errorThrown = false;
    public boolean errorThrownOnUpdate = false;

    public ReportController(DisciplinaryRecordService reportService, StudentService studentService, JoinedSubjectService joinedSubjectService, ProfessorService professorService, DisciplinaryTypeService categoryService, DisciplinarySanctionService disciplinarySanctionService, MeetingService meetingService) {
        this.reportService = reportService;
        this.studentService = studentService;
        this.joinedSubjectService = joinedSubjectService;
        this.professorService = professorService;
        this.categoryService = categoryService;
        this.disciplinarySanctionService = disciplinarySanctionService;
        this.meetingService = meetingService;
    }

    @GetMapping("")
    public String getHomePage(@RequestParam(name = "status", required = false) String status,
                              @RequestParam(name = "professor", required = false) String professor,
                              @RequestParam(name = "subject", required = false) String subject,
                              @RequestParam(name = "meeting", required = false) Long meeting,
                              @RequestParam(name = "suggestedSanction", required = false) String suggestedSanction,
                              @RequestParam(defaultValue = "0") int page,
                              Model model, HttpServletRequest request) {
        int pageSize = 10;

        model.addAttribute("statuses", DisciplinaryStatus.values());
        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("subjects", joinedSubjectService.findAllJoinedSubjects());
        model.addAttribute("meetings", meetingService.getAllMeetings());
        model.addAttribute("sanctions", disciplinarySanctionService.findAllSanctions());

        try {
            Page<DisciplinaryRecord> reportsPage = this.reportService.findAllReports(status, professor, subject, meeting, suggestedSanction, page, pageSize);
            model.addAttribute("allReports", reportsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", reportsPage.getTotalPages());
            model.addAttribute("previousPage", page - 1);
            model.addAttribute("nextPage", page + 1);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 0; i < reportsPage.getTotalPages(); i++) {
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        } catch (ProfessorNotFoundException e) {
            model.addAttribute("errorMessage", "Professor not found.");
        } catch (JoinedSubjectNotFoundException e) {
            model.addAttribute("errorMessage", "Subject not found.");
        }

        return "dosie/reports";
    }

    @PostMapping("/showReports")
    public String showReports(@RequestParam(required = false) String index,
                              Model model, HttpServletRequest request,
                              @RequestParam(defaultValue = "0") int page) throws StudentNotFoundException {
        List<DisciplinaryRecord> reports;
        if (index == null || index.length() == 0)
            return "redirect:/reports";

        try {
            reports = reportService.findAllReportsForStudent(index);
        } catch (StudentNotFoundException studentNotFoundException) {
            model.addAttribute("studentNotFoundFromSearch", true);
            return "dosie/home";
        }

        model.addAttribute("reports", reports);
        return "dosie/reports";
    }

    @GetMapping("/add-report")
    public String showAdd(Model model,
                          HttpServletRequest request) {
        List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
        List<DisciplinaryType> categories = this.categoryService.findAllCategories();

        model.addAttribute("subjects", subjects);
        model.addAttribute("categories", categories);
        model.addAttribute("statuses", DisciplinaryStatus.values());

        return "dosie/add-form";
    }

    @GetMapping("/edit-report/{id}")
    public String showEdit(@PathVariable String id, Model model,
                           HttpServletRequest request) throws DisciplinaryRecordNotFoundException {

        DisciplinaryRecord report = this.reportService.findReportById(id);
        List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
        List<DisciplinaryType> categories = this.categoryService.findAllCategories();

        model.addAttribute("report", report);
        model.addAttribute("subjects", subjects);
        model.addAttribute("categories", categories);
        model.addAttribute("statuses", DisciplinaryStatus.values());
        model.addAttribute("setFalse", false);

        return "dosie/add-form";
    }

    @PostMapping("/add/")
    public String addReport(@RequestParam String student_index,
                            @RequestParam String professor_username,
                            @RequestParam String subject,
                            @RequestParam String category,
                            @RequestParam String degree,
                            @RequestParam String report_note,
                            @RequestParam String report_date,
                            @RequestParam DisciplinaryStatus status,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response
    ) throws InterruptedException, JoinedSubjectNotFoundException, ProfessorNotFoundException, DisciplinaryTypeNotFoundException, StudentNotFoundException {

        errorThrown = false;

        try {
            reportService.createNewReport(Integer.valueOf(degree), report_note, professor_username, student_index, subject, category, status, LocalDate.parse(report_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (ProfessorNotFoundException professorNotFoundException) {
            errorThrown = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("professorError", professorNotFoundException.getMessage());
            return "dosie/add-form";
        } catch (StudentNotFoundException studentNotFoundException) {
            errorThrown = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("studentError", studentNotFoundException.getMessage());
            return "dosie/add-form";
        } catch (JoinedSubjectNotFoundException joinedSubjectNotFoundException) {
            errorThrown = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("subjectError", joinedSubjectNotFoundException.getMessage());
            return "dosie/add-form";
        }

        if (!errorThrown) {
            model.addAttribute("reportCreated", true);
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            return "dosie/add-form";
        }

        return "redirect:/reports";
    }

    @PostMapping("/add/{id}")
    public String editReport(@PathVariable String id,
                             @RequestParam String student_index,
                             @RequestParam String professor_username,
                             @RequestParam String subject,
                             @RequestParam String category,
                             @RequestParam String degree,
                             @RequestParam String report_note,
                             @RequestParam String report_date,
                             @RequestParam DisciplinaryStatus status,
                             Model model,
                             HttpServletResponse servletResponse,
                             HttpServletRequest request) throws JoinedSubjectNotFoundException, ProfessorNotFoundException, DisciplinaryTypeNotFoundException, StudentNotFoundException, DisciplinaryRecordNotFoundException {

        errorThrownOnUpdate = false;

        try {
            this.reportService.updateReport(id, Float.valueOf(degree), report_note, professor_username, student_index, subject, category, status, LocalDate.parse(report_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (ProfessorNotFoundException professorNotFoundException) {
            errorThrownOnUpdate = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("professorError", professorNotFoundException.getMessage());
            model.addAttribute("setFalse", true);
            return "dosie/add-form";
        } catch (StudentNotFoundException studentNotFoundException) {
            errorThrownOnUpdate = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("studentError", studentNotFoundException.getMessage());
            model.addAttribute("setFalse", true);
            return "dosie/add-form";
        } catch (JoinedSubjectNotFoundException joinedSubjectNotFoundException) {
            errorThrownOnUpdate = true;
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("subjectError", joinedSubjectNotFoundException.getMessage());
            model.addAttribute("setFalse", true);
            return "dosie/add-form";
        } catch (DisciplinaryRecordNotFoundException disciplinaryRecordNotFoundException) {
            errorThrownOnUpdate = true;
            model.addAttribute("reportNotFound", disciplinaryRecordNotFoundException.getMessage());
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            model.addAttribute("setFalse", true);
            return "dosie/add-form";
        }

        if (!errorThrownOnUpdate) {
            model.addAttribute("reportUpdated", true);
            List<JoinedSubject> subjects = this.joinedSubjectService.findAllJoinedSubjects();
            List<DisciplinaryType> categories = this.categoryService.findAllCategories();
            model.addAttribute("subjects", subjects);
            model.addAttribute("categories", categories);
            return "dosie/add-form";
        }

        return "redirect:/reports";
    }

    @GetMapping("/review/{id}")
    public String showRecord(@PathVariable String id,
                             Model model) {
        try {

            DisciplinaryRecord disciplinaryRecord = reportService.findReportById(id);
            model.addAttribute("record", disciplinaryRecord);
        } catch (DisciplinaryRecordNotFoundException e) {

            model.addAttribute("recordError", e.getMessage());
        }
        return "dosie/show-record";
    }
}

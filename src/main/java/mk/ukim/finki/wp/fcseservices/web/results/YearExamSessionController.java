package mk.ukim.finki.wp.fcseservices.web.results;


import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;

@Controller
@RequestMapping("/admin/exam-session")
public class YearExamSessionController {

    private final YearExamSessionService service;

    public YearExamSessionController(YearExamSessionService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String session,
                       @RequestParam(required = false) String year,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results) {
        String session1 = session != null && !session.isEmpty() ? session : null;
        Specification<YearExamSession> filter = Specification
                .where(filterExamSession(YearExamSession.class, "session", session1))
                .and(filterContainsText(YearExamSession.class, "year", year));
        System.out.println(session);
        Page<YearExamSession> result = service.findAll(filter, pageNum, results);
        model.addAttribute("page", result);
        model.addAttribute("sessions", ExamSession.values());
        model.addAttribute("selectedSession", session);
        model.addAttribute("selectedYear", year);
        return "results/listYearExamSessions";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("sessions", ExamSession.values());
        model.addAttribute("cycles", StudyCycle.values());
        return "results/addYearExamSession";
    }

    @PostMapping("/")
    public String create(
            @RequestParam ExamSession session,
            @RequestParam String year,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionEnd,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentStartDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentEndDate,
            @RequestParam List<String> cycle) {
        List<StudyCycle> cycles = new ArrayList<>();
        for (String cyc : cycle){
            cycles.add(StudyCycle.valueOf(cyc));
        }
        this.service.create(session, year, sessionStart, sessionEnd, enrollmentStartDate, enrollmentEndDate, cycles);
        return "redirect:/admin/exam-session";
    }

    @GetMapping("/{name}/edit")
    public String showEdit(@PathVariable String name, Model model) {
        model.addAttribute("ses", service.findByName(name).get());
        model.addAttribute("cycles", StudyCycle.values());
        model.addAttribute("sessions", ExamSession.values());

        return "results/addYearExamSession";
    }

    @PostMapping("/{name}/delete")
    public String delete(@PathVariable String name) {
        this.service.delete(name);
        return "redirect:/admin/exam-session";
    }

    @PostMapping("/{name}")
    public String update(
            @PathVariable String name,
            @RequestParam ExamSession session,
            @RequestParam String year,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionStart, @RequestParam LocalDate sessionEnd, @RequestParam LocalDate enrollmentStartDate, @RequestParam LocalDate enrollmentEndDate,
            @RequestParam List<String> cycle) {
        List<StudyCycle> cycles = new ArrayList<>();
        for (String cyc : cycle){
            cycles.add(StudyCycle.valueOf(cyc));
        }
        this.service.update(name, session, year, sessionStart, sessionEnd, enrollmentStartDate, enrollmentEndDate, cycles);
        return "redirect:/admin/exam-session";
    }
}


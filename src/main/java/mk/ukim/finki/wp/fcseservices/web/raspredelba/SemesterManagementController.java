package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterState;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidSemesterYearException;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/semester")
public class SemesterManagementController {

    private final SemesterManagementService semesterManagementService;

    public SemesterManagementController(SemesterManagementService semesterManagementService) {
        this.semesterManagementService = semesterManagementService;
    }

    @GetMapping
    public String getAllSemesters(Model model,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "20") Integer results) {
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        Page<Semester> page = semesterManagementService.list(pageNum,results);
        model.addAttribute("semesters", semesters);
        model.addAttribute("page", page);
        return "raspredelba/semester/listSemesters";
    }

    @GetMapping("/edit/{code}")
    public String showEditSemesterForm(@PathVariable String code, Model model) {
        Semester semester = semesterManagementService.getSemesterById(code).orElseThrow(() -> new IllegalArgumentException(code));
        model.addAttribute("semester", semester);
        return "raspredelba/semester/addSemester";
    }

    @GetMapping("/add")
    public String showSemesterForm() {
        return "raspredelba/semester/addSemester";
    }

    @PostMapping("/save")
    public String saveSemester(@RequestParam String year, @RequestParam SemesterType semesterType, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentStartDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentEndDate,
                               @RequestParam List<StudyCycle> cycles,
                               @RequestParam SemesterState state) {
        if (year.contains("/"))
            throw new InvalidSemesterYearException(year);
        this.semesterManagementService.saveSemester(year, semesterType, startDate, endDate, enrollmentStartDate, enrollmentEndDate, cycles, state);
        return "redirect:/admin/semester";
    }

    @PostMapping("/save/{code}")
    public String saveSemesterByCode(@PathVariable String code, @RequestParam String year, @RequestParam SemesterType semesterType,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentStartDate,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentEndDate,
                                     @RequestParam List<StudyCycle> cycles,
                                     @RequestParam SemesterState state) {
        this.semesterManagementService.updateSemester(code, year, semesterType, startDate, endDate, enrollmentStartDate, enrollmentEndDate, cycles, state);
        return "redirect:/admin/semester";
    }


}

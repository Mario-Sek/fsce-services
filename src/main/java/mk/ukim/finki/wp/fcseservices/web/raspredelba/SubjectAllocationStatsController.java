package mk.ukim.finki.wp.fcseservices.web.raspredelba;



import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import mk.ukim.finki.wp.fcseservices.service.CoursePreferenceService;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.SubjectAllocationStatsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject-allocation-stats")
public class SubjectAllocationStatsController {

    private final SemesterManagementService semesterManagementService;
    private final SubjectAllocationStatsService subjectAllocationStatsService;

    private final CoursePreferenceService coursePreferenceService;

    private final JoinedSubjectService joinedSubjectService;

    public SubjectAllocationStatsController(SemesterManagementService semesterManagementService,
                                            SubjectAllocationStatsService subjectAllocationStatsService,
                                            CoursePreferenceService coursePreferenceService, JoinedSubjectService joinedSubjectService) {
        this.semesterManagementService = semesterManagementService;
        this.subjectAllocationStatsService = subjectAllocationStatsService;
        this.coursePreferenceService = coursePreferenceService;
        this.joinedSubjectService = joinedSubjectService;
    }

    @GetMapping
    public String subjectAllocationStats(Model model,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "100") Integer results,
                                         @RequestParam(required = false) String semester,
                                         @RequestParam(required = false) Integer defaultSemester,
                                         @RequestParam(required = false) String subject,
                                         @RequestParam(required = false) Boolean mentorshipCourse) {
        Page<SubjectAllocationStats> page = subjectAllocationStatsService.list(semester, defaultSemester, subject, mentorshipCourse, pageNum, results);
        Map<JoinedSubject, List<CoursePreference>> coursePref = coursePreferenceService.getAllCoursePreferences().stream().collect(Collectors.groupingBy(CoursePreference::getSubject));
        model.addAttribute("semesters", semesterManagementService.getAllSemesters());
        model.addAttribute("subjects", joinedSubjectService.getAllJoinedSubjects());
        model.addAttribute("coursePref", coursePref);
        model.addAttribute("page", page);
        return "raspredelba/subject-allocation-stats/listSubjectAllocationStats";
    }

    @PostMapping("/calculate")
    public String calculateSASFromSSE(@RequestParam String semester) {
        subjectAllocationStatsService.calculate(semester);
        return "redirect:/admin/subject-allocation-stats";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        subjectAllocationStatsService.deleteById(id);
        return "redirect:/admin/subject-allocation-stats";
    }
}

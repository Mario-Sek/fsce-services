package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/teacher/{tId}/details")
public class TeacherDetailsController {
    @Autowired
    private TeacherSubjectRequestsService teacherSubjectRequestsService;

    @Autowired
    private TeacherSubjectAllocationsService teacherSubjectAllocationsService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SemesterManagementService semesterManagementService;

    @GetMapping
    public String viewTeacherDetails(@PathVariable("tId") String professorId,
                                     @RequestParam(value = "semesterCode", required = false) String semesterCode,
                                     Model model) {
        Professor professor = professorService.findById(professorId);
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        List<TeacherSubjectRequests> requests = teacherSubjectRequestsService.getTeacherSubjectRequestsByProfessorId(professorId);
        List<TeacherSubjectAllocations> allocations;
        List<Course> groups;

        if (semesterCode != null && !semesterCode.isEmpty()) {
            allocations = teacherSubjectAllocationsService.getTeacherSubjectAllocationsByProfessorIdAndSemester(professorId, semesterCode);
            groups = courseService.getCoursesByProfessorIdAndSemester(professorId, professorId, semesterCode);
        } else {
            allocations = teacherSubjectAllocationsService.getTeacherSubjectAllocationsByProfessorId(professorId);
            groups = courseService.getCoursesByProfessorId(professorId);
        }

        model.addAttribute("professor", professor);
        model.addAttribute("requests", requests);
        model.addAttribute("allocations", allocations);
        model.addAttribute("groups", groups);
        model.addAttribute("semesters", semesters);

        return "raspredelba/teacher-details/teacher-details";
    }
}

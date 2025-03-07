package mk.ukim.finki.wp.fcseservices.web.raspredelba.rest;


import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectAllocationsDTO;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@AllArgsConstructor
public class TeacherAllocationRestController {

    private final JoinedSubjectService joinedSubjectService;
    private final SubjectAllocationStatsService subjectAllocationStatsService;
    private final TeacherSubjectAllocationsService teacherSubjectAllocationsService;
    private final ProfessorService professorService;

    private final SemesterManagementService semesterManagementService;
    private final TeacherSubjectRequestsService teacherSubjectRequestsService;

    private final CourseService courseService;

    @GetMapping("/joined-subjects")
    public List<JoinedSubject> getAllJoinedSubjects() {
        return this.joinedSubjectService.getAllJoinedSubjects();
    }


    @GetMapping("/subject-allocation-stats")
    public List<SubjectAllocationStats> getAllSubjectAllocationStats(@RequestParam String semester) {
        return subjectAllocationStatsService.getAllSubjectAllocationStats(semester);
    }

    @GetMapping("/teacher-subject-allocations/{subjectId}")
    public List<TeacherSubjectAllocations> getAllTeacherSubjectAllocationsBySubject(@PathVariable String subjectId) {
        return teacherSubjectAllocationsService.getAllBySubject(subjectId);
    }

    @GetMapping("/teacher-subject-requests")
    public List<TeacherSubjectRequests> getAllTeacherSubjectRequests() {
        return teacherSubjectRequestsService.getAllTeacherSubjectRequests();
    }


    @GetMapping("/teacher-subject-allocations/semester/{semesterCode}")
    public List<TeacherSubjectAllocations> getAllTeacherSubjectAllocationsBySemester(@PathVariable String semesterCode) {
        return teacherSubjectAllocationsService.getAllTeacherSubjectAllocationsBySemester(semesterCode);
    }

    @GetMapping("/semesters")
    public List<Semester> getAllSemesters() {
        return semesterManagementService.getAllSemesters();
    }


    @PutMapping("/subject-allocation-stats/number-of-groups/{id}/{numberOfGroups}")
    public SubjectAllocationStats editNumberOfGroupsForSubjectAllocationStats(@PathVariable String id,
                                                                              @PathVariable Integer numberOfGroups,
                                                                              @RequestParam(defaultValue = "false") Boolean mentorshipCourse) {
        return subjectAllocationStatsService.editNumberOfGroups(id, numberOfGroups, mentorshipCourse);
    }

    @PostMapping("/teacher-subject-allocations/add")
    public TeacherSubjectAllocations addTeacherSubjectAllocation(
            @RequestBody TeacherSubjectAllocationsDTO newAllocation,
            @RequestParam("semester") String semester) {
        System.out.println(semester);
        return teacherSubjectAllocationsService.addTeacherSubjectAllocation(newAllocation, semester);
    }

    @PutMapping("/teacher-subject-allocations/edit")
    public ResponseEntity<String> editTeacherSubjectAllocations(@RequestBody TeacherSubjectAllocations editedAllocation) {

        teacherSubjectAllocationsService.editTeacherSubjectAllocation(editedAllocation);

        return ResponseEntity.ok("Allocations updated successfully");
    }

    @DeleteMapping("/teacher-subject-allocations/delete/{id}")
    public ResponseEntity<String> deleteTeacherSubjectAllocation(@PathVariable Long id) {
        teacherSubjectAllocationsService.deleteTeacherSubjectAllocations(id);
        return ResponseEntity.ok("Allocation deleted successfully");
    }


    @GetMapping("/professors")
    public List<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }


    @PostMapping("/courses/save/{id}")
    public void save(@PathVariable Long id,
                     @RequestParam String professors,
                     @RequestParam String assistants,
                     @RequestParam String groups,
                     @RequestParam(defaultValue = "false") Boolean english) {
        courseService.save(id, professors, assistants, groups, english);

    }

}

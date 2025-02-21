package mk.ukim.finki.wp.fcseservices.web.raspredelba;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidSemesterException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidSubjectAllocationIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;


@AllArgsConstructor
@Controller
@RequestMapping("/admin/subject-teacher-allocation")
public class TeacherSubjectAllocationsController {
    private final TeacherSubjectAllocationsService teacherSubjectAllocationService;
    private final SemesterManagementService semesterManagementService;
    private final ProfessorService professorService;
    private final JoinedSubjectService joinedSubjectService;
    private final SubjectAllocationStatsService subjectStatsService;
    private final ImportRepository importRepository;
    private final TeacherSubjectRequestsService teacherSubjectRequestsService;
    private final CoursePreferenceService coursePreferenceService;
    private final CourseService courseService;
    private final CourseSizeService courseSizeService;


    @GetMapping
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results,
                       @RequestParam(required = false) String semester,
                       @RequestParam(required = false) String subject,
                       @RequestParam(required = false) String professor,
                       Model model) {

        Specification<TeacherSubjectAllocations> filter = Specification
                .where(filterEquals(TeacherSubjectAllocations.class, "semester.code", semester))
                .and(filterEquals(TeacherSubjectAllocations.class, "subject.abbreviation", subject))
                .and(filterEquals(TeacherSubjectAllocations.class, "professor.id", professor));

        Page<TeacherSubjectAllocations> page = teacherSubjectAllocationService.findAll(filter, pageNum, results);
        List<Professor> professors = professorService.getAllProfessors();
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        List<JoinedSubject> joinedSubjects = joinedSubjectService.getAllJoinedSubjects();

        model.addAttribute("subjects", joinedSubjects);
        model.addAttribute("professors", professors);
        model.addAttribute("semesters", semesters);
        model.addAttribute("semester", semester);
        model.addAttribute("page", page);


        return "raspredelba/teacher-subject-allocations/list";
    }

    @GetMapping("/{joined-subject-id}")
    public String viewSubjectTeacherAllocations(@PathVariable("joined-subject-id") String joinedSubjectId,
                                                @RequestParam(required = false) String semester,
                                                Model model) {
        Semester activeSemester = getActiveSemester(semester);
        JoinedSubject subject = joinedSubjectService.getByAbbreviation(joinedSubjectId);
        List<TeacherSubjectAllocations> allocations = teacherSubjectAllocationService.getAllocationsForSubjectInSemester(subject, activeSemester);
        List<Professor> professors = professorService.getAllProfessors();
        List<Professor> professorsWithClasses = teacherSubjectAllocationService.findProfessorsBySubjectAndSemester(joinedSubjectId, "2023-24-S");
        List<Semester> semesters = semesterManagementService.getAllSemesters();


        List<TeacherSubjectRequests> requests = teacherSubjectRequestsService.getTeacherSubjectRequestsBySubject(subject);
        List<CoursePreference> preferences = coursePreferenceService.getCoursePreferencesBySubject(joinedSubjectId);
        List<Course> courses = courseService.getCoursesBySubjectAndSemester(joinedSubjectId, activeSemester.getCode());
        List<CourseSize> courseSizes = courseSizeService.getCourseSizesBySubjectAndSemester(joinedSubjectId, activeSemester.getCode());


        Map<Semester, List<TeacherSubjectAllocations>> allocationsBySemester = Map.of(activeSemester, allocations);


        // sorted by groups
        Map<Semester, List<Course>> groupsBySemester = Map.of(
                activeSemester,
                courses.stream().sorted(comparing(Course::getGroups)).toList()
        );

        Map<String, List<CourseSize>> groupSizesBySemester = Map.of(activeSemester.getCode(), courseSizes);

        SubjectAllocationStats stats = subjectStatsService.findBySemesterAndSubject(activeSemester.getCode(), subject.getAbbreviation()).orElse(null);


        Map<Semester, SubjectAllocationStats> semesterStats = stats == null ?
                Map.of() :
                Map.of(
                        activeSemester,
                        stats
                );


        model.addAttribute("defaultSemesterCode", activeSemester);
        model.addAttribute("professors", professors);
        model.addAttribute("semesters", semesters);
        model.addAttribute("subjectAllocations", allocations);
        model.addAttribute("joinedSubjectId", joinedSubjectId);
        model.addAttribute("joinedSubject", subject);
        model.addAttribute("allocationsBySemester", allocationsBySemester);
        model.addAttribute("teacherSubjectAllocation", new TeacherSubjectAllocations());
        model.addAttribute("semesterStats", semesterStats);
        model.addAttribute("teacherSubjectRequests", requests);
        model.addAttribute("coursePreferences", preferences);
        model.addAttribute("courses", groupsBySemester);
        model.addAttribute("courseSizes", groupSizesBySemester);
        model.addAttribute("professorsWithClasses", professorsWithClasses);

        return "raspredelba/teacher-subject-allocations/listTeacherSubjectAllocations";
    }

    private Semester getActiveSemester(String semesterCode) {
        if (semesterCode == null || semesterCode.isEmpty()) {
            return semesterManagementService.getActiveSemester();
        } else {
            return semesterManagementService.getSemesterById(semesterCode).orElseThrow(() -> new InvalidSemesterException(semesterCode));
        }
    }

    @GetMapping("/{joined-subject-id}/history")
    public String viewSubjectTeacherAllocationsHistory(
            @PathVariable("joined-subject-id") String joinedSubjectId,
            @RequestParam(required = false) String semesterCode,
            Model model
    ) {
        JoinedSubject subject = joinedSubjectService.getByAbbreviation(joinedSubjectId);
        List<TeacherSubjectAllocations> allocations = teacherSubjectAllocationService.getAllocationsForSubject(subject);
        List<Professor> professors = professorService.getAllProfessors();
        List<Semester> semesters = semesterManagementService.getAllSemesters();


        List<TeacherSubjectRequests> requests = teacherSubjectRequestsService.getTeacherSubjectRequestsBySubject(subject);
        List<CoursePreference> preferences = coursePreferenceService.getCoursePreferencesBySubject(joinedSubjectId);
        List<Course> groups = courseService.getCoursesBySubject(joinedSubjectId);
        List<CourseSize> sizes = courseSizeService.getCourseSizesBySubject(joinedSubjectId);


        Map<Semester, List<TeacherSubjectAllocations>> allocationsBySemester = allocations.stream()
                .filter(alloc -> alloc.getSemester() != null)
                .collect(groupingBy(TeacherSubjectAllocations::getSemester));


        Map<Semester, List<TeacherSubjectAllocations>> allocationsByActiveSemester = allocations.stream()
                .filter(alloc -> alloc.getSemester() != null)
                .collect(groupingBy(TeacherSubjectAllocations::getSemester));
        model.addAttribute("allocationsByActiveSemester", allocationsByActiveSemester);

        Map<Semester, List<TeacherSubjectAllocations>> allocationsByINActiveSemester = allocations.stream()
                .filter(alloc -> alloc.getSemester() != null)
                .collect(groupingBy(TeacherSubjectAllocations::getSemester));
        model.addAttribute("allocationsByINActiveSemester", allocationsByINActiveSemester);


        Map<Semester, List<Course>> groupsBySemester = groups.stream()
                .collect(Collectors.groupingBy(
                        Course::getSemester,
                        Collectors.collectingAndThen(
                                Collectors.toCollection(ArrayList::new), l -> {
                                    l.sort(comparing(Course::getGroups));
                                    return l;
                                })));

        Map<String, List<CourseSize>> groupSizesBySemester = sizes.stream()
                .collect(Collectors.groupingBy(CourseSize::getSemesterCode));

        Map<Semester, SubjectAllocationStats> semesterStats = allocationsBySemester.keySet().stream()
                .map(it -> subjectStatsService.findBySemesterAndSubject(it.getCode(), subject.getAbbreviation()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SubjectAllocationStats::getSemester, it -> it));


        model.addAttribute("professors", professors);
        model.addAttribute("semesters", semesters);
        model.addAttribute("subjectAllocations", allocations);
        model.addAttribute("joinedSubjectId", joinedSubjectId);
        model.addAttribute("joinedSubject", subject);
        model.addAttribute("allocationsBySemester", allocationsBySemester);
        model.addAttribute("teacherSubjectAllocation", new TeacherSubjectAllocations());
        model.addAttribute("semesterStats", semesterStats);
        model.addAttribute("teacherSubjectRequests", requests);
        model.addAttribute("coursePreferences", preferences);
        model.addAttribute("courses", groupsBySemester);
        model.addAttribute("courseSizes", groupSizesBySemester);
        return "raspredelba/teacher-subject-allocations/listTeacherSubjectAllocationsHistory";
    }


    @PostMapping("/{joined-subject-id}/save")
    public String saveTeacherSubjectAllocation(@ModelAttribute("teacherSubjectAllocation") TeacherSubjectAllocations allocation,
                                               @PathVariable("joined-subject-id") String joinedSubjectId,
                                               @RequestParam("professorId") String professorId,
                                               @RequestParam("semesterId") String semesterId,
                                               @RequestParam("numberOfLectureGroups") Float numberOfLectureGroups,
                                               @RequestParam("numberOfExerciseGroups") Float numberOfExerciseGroups,
                                               @RequestParam("numberOfLabGroups") Float numberOfLabGroups,
                                               @RequestParam(value = "englishGroup", defaultValue = "false") Boolean englishGroup,
                                               HttpSession session) {

        TeacherSubjectAllocations teacherSubjectAllocation = new TeacherSubjectAllocations();
        JoinedSubject subject = joinedSubjectService.getByAbbreviation(joinedSubjectId);
        teacherSubjectAllocation.setSubject(subject);

        Professor professor = professorService.getProfessorById(professorId);
        teacherSubjectAllocation.setProfessor(professor);

        Semester semester = semesterManagementService.getSemesterById(semesterId).orElseThrow(() -> new IllegalArgumentException(semesterId));
        teacherSubjectAllocation.setSemester(semester);

        teacherSubjectAllocation.setNumberOfLectureGroups(numberOfLectureGroups);
        teacherSubjectAllocation.setNumberOfExerciseGroups(numberOfExerciseGroups);
        teacherSubjectAllocation.setNumberOfLabGroups(numberOfLabGroups);
        teacherSubjectAllocation.setEnglishGroup(englishGroup);

        teacherSubjectAllocationService.save(teacherSubjectAllocation);
        session.setAttribute("defaultSemesterCode", semesterId);

        return "redirect:/admin/subject-teacher-allocation/{joined-subject-id}";
    }

    @PostMapping("/{id}/update")
    public String updateTeacherSubjectAllocation(@ModelAttribute("teacherSubjectAllocation") TeacherSubjectAllocations allocation,
                                                 @PathVariable Long id,
                                                 @RequestParam(value = "joined-subject-id", required = false) String joinedSubjectId,
                                                 @RequestParam("numberOfLectureGroups") Float numberOfLectureGroups,
                                                 @RequestParam("numberOfExerciseGroups") Float numberOfExerciseGroups,
                                                 @RequestParam("numberOfLabGroups") Float numberOfLabGroups,
                                                 @RequestParam(value = "englishGroup", defaultValue = "false") Boolean englishGroup,
                                                 @RequestHeader(value = "referer", required = false) String referer) {

        TeacherSubjectAllocations teacherSubjectAllocation = teacherSubjectAllocationService.findById(id)
                .orElseThrow(() -> new InvalidSubjectAllocationIdException(id));
        teacherSubjectAllocation.setNumberOfLectureGroups(numberOfLectureGroups);
        teacherSubjectAllocation.setNumberOfExerciseGroups(numberOfExerciseGroups);
        teacherSubjectAllocation.setNumberOfLabGroups(numberOfLabGroups);
        teacherSubjectAllocation.setEnglishGroup(englishGroup);

        teacherSubjectAllocationService.save(teacherSubjectAllocation);

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/subject-teacher-allocation";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAllocation(@PathVariable Long id,
                                   @RequestParam(value = "joined-subject-id", required = false) String joinedSubjectId,
                                   @RequestHeader(value = "referer", required = false) String referer) {
        teacherSubjectAllocationService.delete(id);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/subject-teacher-allocation";
        }
    }

    @PostMapping("/validate")
    public String validateAllAllocations(@RequestParam("semesterCode") String semesterCode, RedirectAttributes redirectAttributes) {
        boolean validationStatus = teacherSubjectAllocationService.validateAllocationsForSemester(semesterCode);
        if (validationStatus) {
            redirectAttributes.addAttribute("validationSuccess", validationStatus);
        } else {
            redirectAttributes.addAttribute("validationFailed", validationStatus);
        }
        redirectAttributes.addAttribute("semester", semesterCode);
        return "redirect:/admin/subject-teacher-allocation";
    }

    @GetMapping("/clone")
    public String cloneTeacherSubjectAllocationPage(Model model) {
        model.addAttribute("semesters", semesterManagementService.getAllSemesters());
        return "raspredelba/teacher-subject-allocations/clone";
    }

    @PostMapping("/clone")
    public String cloneTeacherSubjectAllocation(@RequestParam String semesterFrom,
                                                @RequestParam String semesterTo) {
        teacherSubjectAllocationService.cloneTeacherSubjectAllocations(semesterFrom, semesterTo);
        return "redirect:/admin/subject-teacher-allocation";
    }

    @PostMapping("/delete-by-semester")
    public String deleteBySemester(@RequestParam String semesterCode) {
        teacherSubjectAllocationService.deleteBySemester(semesterCode);
        return "redirect:/admin/subject-teacher-allocation";
    }

    @GetMapping("/export")
    public void export(@RequestParam(required = false) String semesterCode, HttpServletResponse response) {
        Specification<TeacherSubjectAllocations> spec = Specification
                .where(filterEquals(TeacherSubjectAllocations.class, "semester.code", semesterCode));
        List<TeacherSubjectAllocations> list = teacherSubjectAllocationService.findAll(spec, 1, 10_000)
                .getContent();

        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"teacher_subject_allocations.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(TeacherSubjectAllocations.class, list, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/import")
    public void importGroups(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<TeacherSubjectAllocations> tsa = importRepository.readTypeList(file, TeacherSubjectAllocations.class);

        List<TeacherSubjectAllocations> invalidEnrollments = teacherSubjectAllocationService.importFromList(tsa);
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"student_groups.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(TeacherSubjectAllocations.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

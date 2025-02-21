package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.dto.CoursePreferenceDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidCoursePreferenceIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.CoursePreferenceService;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterContainsText;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEqualsV;
import static org.springframework.data.jpa.domain.Specification.where;

@Controller
@RequestMapping("/admin/course-preferences")
public class CoursePreferenceController {

    private final CoursePreferenceService coursePreferenceService;
    private final JoinedSubjectService subjectService;

    private final ImportRepository importRepository;
    private final SemesterManagementService semesterManagementService;

    public CoursePreferenceController(CoursePreferenceService coursePreferenceService,
                                      JoinedSubjectService subjectService, ImportRepository importRepository, SemesterManagementService semesterManagementService) {
        this.coursePreferenceService = coursePreferenceService;
        this.subjectService = subjectService;
        this.importRepository = importRepository;
        this.semesterManagementService = semesterManagementService;
    }


    @GetMapping
    public String getCoursePreferences(Model model,
                                       @RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "20") Integer results,
                                       @RequestParam(required = false) String id,
                                       @RequestParam(required = false) String subjectName,
                                       @RequestParam(required = false) String lectureSharing,
                                       @RequestParam(required = false) String auditoriumExercisesSharing,
                                       @RequestParam(required = false) Boolean preferOnlineLectures,
                                       @RequestParam(required = false) Boolean preferOnlineExercises,
                                       @RequestParam(required = false) Boolean labExercisesAsConsultations) {

        Page<CoursePreference> coursePreferencesPage;

        Specification<CoursePreference> spec = where(filterContainsText(CoursePreference.class, "subject.name", subjectName));

        spec = spec.and(filterEqualsV(CoursePreference.class, "lectureSharing", lectureSharing != null && !lectureSharing.isEmpty() ? LectureSharing.valueOf(lectureSharing) : null))
                .and(filterEqualsV(CoursePreference.class, "auditoriumExercisesSharing", auditoriumExercisesSharing != null && !auditoriumExercisesSharing.isEmpty() ? LectureSharing.valueOf(auditoriumExercisesSharing) : null))
                .and(filterEqualsV(CoursePreference.class, "preferOnlineLectures", preferOnlineLectures))
                .and(filterEqualsV(CoursePreference.class, "preferOnlineExercises", preferOnlineExercises))
                .and(filterEqualsV(CoursePreference.class, "labExercisesAsConsultations", labExercisesAsConsultations));


        coursePreferencesPage = coursePreferenceService.list(spec, pageNum, results);

        model.addAttribute("page", coursePreferencesPage);

        model.addAttribute("coursePreferences", coursePreferenceService.getAllCoursePreferences());
        List<JoinedSubject> subjects = subjectService.getAllJoinedSubjects();


        model.addAttribute("subjects", subjects);
        model.addAttribute("semesters", semesterManagementService.getAllSemesters());

        return "raspredelba/course-preferences/listCoursePreferences";
    }

    @GetMapping(params = "export")
    public void export(Model model,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "2000") Integer results,
                       @RequestParam(required = false) String subjectName,
                       @RequestParam(required = false) String lectureSharing,
                       @RequestParam(required = false) String auditoriumExercisesSharing,
                       @RequestParam(required = false) Boolean preferOnlineLectures,
                       @RequestParam(required = false) Boolean preferOnlineExercises,
                       @RequestParam(required = false) Boolean labExercisesAsConsultations,
                       HttpServletResponse response) {

        Page<CoursePreference> page;

        Specification<CoursePreference> spec = where(filterContainsText(CoursePreference.class, "subject.name", subjectName));


        spec = spec.and(filterEqualsV(CoursePreference.class, "lectureSharing", lectureSharing != null && !lectureSharing.isEmpty() ? LectureSharing.valueOf(lectureSharing) : null))
                .and(filterEqualsV(CoursePreference.class, "auditoriumExercisesSharing", auditoriumExercisesSharing != null && !auditoriumExercisesSharing.isEmpty() ? LectureSharing.valueOf(auditoriumExercisesSharing) : null))
                .and(filterEqualsV(CoursePreference.class, "preferOnlineLectures", preferOnlineLectures))
                .and(filterEqualsV(CoursePreference.class, "preferOnlineExercises", preferOnlineExercises))
                .and(filterEqualsV(CoursePreference.class, "labExercisesAsConsultations", labExercisesAsConsultations));


        page = coursePreferenceService.list(spec, pageNum, results);

        doExport(response, page.getContent().stream().map(it -> new CoursePreferenceDTO(
                it.getSubject().getName(),
                it.getLectureSharing().name(),
                it.getAuditoriumExercisesSharing().name(),
                it.isPreferOnlineLectures(),
                it.isPreferOnlineExercises(),
                it.isLabExercisesAsConsultations(),
                it.getSubject().getName()
        )).collect(Collectors.toList()));
    }

    @PostMapping("/import")
    public void importCoursePreferences(@RequestParam String semester, @RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<CoursePreferenceDTO> preferences = importRepository.readPreferences(file, CoursePreferenceDTO.class);

        List<CoursePreferenceDTO> invalidPreferences = coursePreferenceService.importCoursePreferences(preferences, semester);

        String fileName = "invalid_preferences_" + semester + ".tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writePreferences(CoursePreferenceDTO.class, invalidPreferences, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/add")
    public String showCoursePreferenceForm(Model model,
                                           @RequestParam(required = false) String joinedSubject) {
        model.addAttribute("coursePreference", new CoursePreference());
        List<JoinedSubject> subjects = subjectService.getAllJoinedSubjects();
        model.addAttribute("subjects", subjects);
        model.addAttribute("joinedSubjectAbbreviation", joinedSubject);
        return "raspredelba/course-preferences/addCoursePreferenceForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditCoursePreferenceForm(@PathVariable String id, Model model, @RequestHeader(value = "referer", required = false) String referer) {
        try {
            CoursePreference coursePreference = coursePreferenceService.getCoursePreferenceById(id);
            List<JoinedSubject> subjects = subjectService.getAllJoinedSubjects();
            model.addAttribute("subjects", subjects);
            model.addAttribute("coursePreference", coursePreference);
            model.addAttribute("referer", referer);
            return "raspredelba/course-preferences/editCoursePreferenceForm";
        } catch (InvalidCoursePreferenceIdException e) {
            return "redirect:/admin/course-preferences/add?sharing=STUDENT_GROUPS&joinedSubject=" + id;
        }
    }

    @PostMapping("/save")
    public String saveCoursePreference(@RequestParam(required = false) JoinedSubject subject,
                                       @RequestParam(required = false) LectureSharing lectureSharing,
                                       @RequestParam LectureSharing auditoriumExercisesSharing,
                                       @RequestParam(required = false) boolean preferOnlineLectures,
                                       @RequestParam(required = false) boolean preferOnlineExercises,
                                       @RequestParam(required = false) boolean labExercisesAsConsultations,
                                       @RequestParam(value = "referer", required = false) String referer) {

        coursePreferenceService.saveCoursePreference(subject, lectureSharing, auditoriumExercisesSharing, preferOnlineLectures, preferOnlineExercises, labExercisesAsConsultations);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/course-preferences";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCoursePreference(@PathVariable String id, @RequestHeader(value = "referer", required = false) String referer) {
        coursePreferenceService.deleteCoursePreference(id);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/course-preferences";
        }
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<CoursePreferenceDTO> example = new ArrayList<>();
        example.add(new CoursePreferenceDTO("Вовед во когнитивни науки", "STUDENT_GROUPS", "TOPICS", true, true, false, null));
        example.add(new CoursePreferenceDTO("Обработка на природните јазици", "STUDENT_GROUPS", "STUDENT_GROUPS", true, true, true, null));
        example.add(new CoursePreferenceDTO("Оперативни системи", "NONE", "NONE", false, true, true, null));

        doExport(response, example);
    }

    @GetMapping("/display/{id}")
    public String displayCoursePreference(@PathVariable String id, Model model) {
        CoursePreference coursePreference = coursePreferenceService.getCoursePreferenceById(id);
        model.addAttribute("coursePreference", coursePreference);
        return "raspredelba/course-preferences/displayCoursePreference";
    }

    public void doExport(HttpServletResponse response, List<CoursePreferenceDTO> data) {
        String fileName = "course_preferences.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(CoursePreferenceDTO.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("restore/{id}")
    public String restore(@PathVariable String id,
                          @RequestParam LectureSharing lectureSharing,
                          @RequestParam LectureSharing auditoriumExercisesSharing,
                          @RequestParam(required = false) boolean preferOnlineLectures,
                          @RequestParam(required = false) boolean preferOnlineExercises,
                          @RequestParam(required = false) boolean labExercisesAsConsultations
    ) {

        CoursePreference coursePreference = coursePreferenceService.getCoursePreferenceById(id);
        coursePreference.setLectureSharing(lectureSharing);
        coursePreference.setAuditoriumExercisesSharing(auditoriumExercisesSharing);
        coursePreference.setPreferOnlineLectures(preferOnlineLectures);
        coursePreference.setPreferOnlineExercises(preferOnlineExercises);
        coursePreference.setLabExercisesAsConsultations(labExercisesAsConsultations);
        coursePreferenceService.save(coursePreference);
        return "redirect:/admin/course-preferences/display/" + id;
    }

}


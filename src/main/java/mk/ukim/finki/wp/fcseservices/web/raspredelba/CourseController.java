package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.dto.CourseDto;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.CoursePreferenceRepository;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.repository.StudentGroupRepository;
import mk.ukim.finki.wp.fcseservices.service.CourseService;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/courses")
public class CourseController {

    private final CourseService service;

    private final ImportRepository importRepository;
    private final JoinedSubjectService joinedSubjectService;
    private final ProfessorService professorService;
    private final SemesterManagementService semesterService;
    private final StudentGroupRepository studentGroupRepository;
    private final CoursePreferenceRepository coursePreferenceRepository;


    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String semesterCode,
                       @RequestParam(required = false) String joinedSubject,
                       @RequestParam(required = false) String groupName,
                       @RequestParam(required = false) String professor,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results) {

        Page<Course> result = service.list(semesterCode, joinedSubject, groupName, professor, pageNum, results);

        model.addAttribute("page", result);
        model.addAttribute("subjects", joinedSubjectService.getAllJoinedSubjects());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("studentGroups", studentGroupRepository.findAll());

        return "raspredelba/courses/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("course", new Course());

        model.addAttribute("subjects", joinedSubjectService.getAllJoinedSubjects());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("semesters", semesterService.getAllSemesters());

        return "raspredelba/courses/add";
    }


    @PostMapping("/calculate")
    public String calculate(@RequestParam String semesterCode) {
        service.calculateGroups(semesterCode);
        return "redirect:/admin/courses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestHeader(value = "referer", required = false) String referer) {
        service.delete(id);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/courses";
        }
    }

    @PostMapping("/save")
    public String delete(@RequestParam String semester,
                         @RequestParam String joinedSubject,
                         @RequestParam(required = false) String professors,
                         @RequestParam(required = false) String assistants,
                         @RequestParam(required = false) String groups) {
        service.save(semester, joinedSubject, professors, assistants, groups);
        return "redirect:/admin/courses";
    }

    @GetMapping("/create")
    public String create(@RequestParam String semester,
                         @RequestParam String joinedSubject,
                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("semester", semester);
        redirectAttributes.addAttribute("joinedSubject", joinedSubject);
        service.createAndCalculate(semester, joinedSubject);
        return "redirect:/admin/courses";
    }


    @PostMapping("/import")
    public void importStudents(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<CourseDto> data = importRepository.readTypeList(file, CourseDto.class);
        List<CourseDto> invalid = service.importData(data);
        String fileName = "invalid_courses.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(CourseDto.class, invalid, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<CourseDto> example = new ArrayList<>();
        example.add(new CourseDto(
                "2022_2023_W",
                "WP",
                "Веб програмирање",
                "dimitar.trajanov;sasho.gramatikov;riste.stojanov;kostadin.mishev",
                "kostadin.mishev;ana.todorovska",
                400,
                100,
                1.0F,
                "",
                false,
                LectureSharing.NONE,
                LectureSharing.NONE,
                ""
        ));

        String fileName = "course-sample.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(CourseDto.class, example, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(params = "export")
    public void export(HttpServletResponse response,
                       @RequestParam(required = false) String semesterCode,
                       @RequestParam(required = false) String joinedSubject,
                       @RequestParam(required = false) String groupName,
                       @RequestParam(required = false) String professor,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "2000") Integer results) {

        Page<Course> page = service.list(semesterCode, joinedSubject, groupName, professor, pageNum, results);
        doExport(response, page.getContent().stream()
                .map(it -> new CourseDto(it,
                        coursePreferenceRepository.findById(it.getJoinedSubject().getAbbreviation()).orElse(null))
                )
                .collect(Collectors.toList()));
    }


    @GetMapping("/export-schedule")
    public void export(@RequestParam String semesterCode, HttpServletResponse response) {
        String tsv = service.toTsv(service.list(semesterCode, null, null, null, 1, 10000).getContent());

        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"schedule_import.tsv\"");

        try (BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            outputStream.write(tsv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void doExport(HttpServletResponse response, List<CourseDto> data) {
        String fileName = "courses.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(CourseDto.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

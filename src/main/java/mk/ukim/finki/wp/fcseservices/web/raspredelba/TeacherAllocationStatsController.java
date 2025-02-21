package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherAllocationStats;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.TeacherAllocationStatsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/teacher-allocation-stats")
public class TeacherAllocationStatsController {

    private final TeacherAllocationStatsService service;
    private final ProfessorService professorService;
    private final JoinedSubjectService joinedSubjectService;
    private final SemesterManagementService semesterService;
    private final ImportRepository importRepository;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "100") Integer results,
                       @RequestParam(required = false) String semesterCode,
                       @RequestParam(required = false) String subjectAbbreviation,
                       @RequestParam(required = false) String professorId,
                       @RequestParam(required = false) ProfessorTitle professorTitle) {

        Page<TeacherAllocationStats> page = service.list(semesterCode,
                subjectAbbreviation,
                professorId,
                professorTitle,
                pageNum, results);

        model.addAttribute("page", page);

        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("subjects", joinedSubjectService.getAllJoinedSubjects());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("titles", ProfessorTitle.values());


        return "raspredelba/teacher-allocation-stats/list";
    }

    @GetMapping("/export")
    public void sampleTsv(HttpServletResponse response) {
        List<TeacherAllocationStats> list = service.findAll();

        String fileName = "students_example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"teacher_stats.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(TeacherAllocationStats.class, list, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

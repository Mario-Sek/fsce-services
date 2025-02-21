package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorRepository;
import mk.ukim.finki.wp.fcseservices.repository.SemesterRepository;
import mk.ukim.finki.wp.fcseservices.service.CourseSizeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/course-size")
public class CourseSizeController {

    private final CourseSizeService service;

    private final ImportRepository importRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final ProfessorRepository professorRepository;
    private final SemesterRepository semesterRepository;


    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String semester,
                       @RequestParam(required = false) String joinedSubject,
                       @RequestParam(required = false) Long groupId,
                       @RequestParam(required = false) String professor,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results) {

        Page<CourseSize> result = service.list(semester, joinedSubject, groupId, pageNum, results);

        model.addAttribute("page", result);
        model.addAttribute("subjects", joinedSubjectRepository.findAll());
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());

        return "raspredelba/course-size/list";
    }

}

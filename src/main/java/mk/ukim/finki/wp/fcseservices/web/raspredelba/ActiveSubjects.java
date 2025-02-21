package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectProfessorsDTO;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.SubjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActiveSubjects {

    private final JoinedSubjectService joinedSubjectService;
    private final SubjectService subjectService;

    private final String subjectDetailsPath;

    public ActiveSubjects(JoinedSubjectService joinedSubjectService,
                          SubjectService subjectService,
                          @Value("${finki.akreditacii.subject-url-prefix}") String subjectDetailsPath) {
        this.joinedSubjectService = joinedSubjectService;
        this.subjectService = subjectService;
        this.subjectDetailsPath = subjectDetailsPath;
    }

    @ModelAttribute(name = "subjectDetailsPath")
    public String subjectDetailsPath() {
        return subjectDetailsPath;
    }

    @GetMapping("/active-subjects")
    public String getActiveSubjects(Model model,
                                    @RequestParam(defaultValue = "") String name,
                                    @RequestParam(defaultValue = "") SemesterType semesterType,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "20") Integer results) {

        //Page<JoinedSubject> activeSubjectsPage = joinedSubjectService.listActivatedSubjects(name, semesterType, pageNum, results);
        Page<JoinedSubjectProfessorsDTO> activeSubjectsPage = joinedSubjectService.listActivatedSubjectsWithProfessors(name, semesterType, pageNum, results);
        model.addAttribute("page", activeSubjectsPage);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "raspredelba/joined-subjects/listActiveSubjects";
    }
}

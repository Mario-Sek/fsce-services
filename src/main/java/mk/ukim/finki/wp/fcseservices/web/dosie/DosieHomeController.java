package mk.ukim.finki.wp.fcseservices.web.dosie;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.service.DisciplinarySanctionService;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.MeetingService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DosieHomeController {

    private final ProfessorService professorService;
    private final JoinedSubjectService joinedSubjectService;
    private final MeetingService meetingService;
    private final DisciplinarySanctionService disciplinarySanctionService;


    public DosieHomeController(ProfessorService professorService, JoinedSubjectService joinedSubjectService, MeetingService meetingService, DisciplinarySanctionService disciplinarySanctionService) {
        this.professorService = professorService;
        this.joinedSubjectService = joinedSubjectService;
        this.meetingService = meetingService;
        this.disciplinarySanctionService = disciplinarySanctionService;
    }

    @GetMapping("/dosie")
    public String home(Model model, Authentication authentication) {

        String username = authentication.getName();

        model.addAttribute("username", username);
        model.addAttribute("statuses", DisciplinaryStatus.values());
        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("subjects", joinedSubjectService.findAllJoinedSubjects());
        model.addAttribute("meetings", meetingService.getAllMeetings());
        model.addAttribute("sanctions", disciplinarySanctionService.findAllSanctions());
        return "dosie/home";
    }
}

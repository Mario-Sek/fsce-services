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

    @GetMapping("/dosie")
    public String home(Model model) {
        return "dosie/home";
    }
}

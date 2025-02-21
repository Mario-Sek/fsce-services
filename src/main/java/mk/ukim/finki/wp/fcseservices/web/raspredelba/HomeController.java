package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/raspredelba"})
    public String home() {
        return "raspredelba/index";
    }
}

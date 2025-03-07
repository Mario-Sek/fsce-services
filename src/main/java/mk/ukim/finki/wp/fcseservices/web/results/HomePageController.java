package mk.ukim.finki.wp.fcseservices.web.results;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/results-management")
public class HomePageController {
    @GetMapping
    public String homePage() {
        return "results/home";
    }
}

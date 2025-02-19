package mk.ukim.finki.wp.fcseservices.web.projects;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectsHomeController {

    @RequestMapping("/projects")
    String getProjectsHome(){
        return "projects/home";
    }
}

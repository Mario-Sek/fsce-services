package mk.ukim.finki.wp.fcseservices.web.projects;


import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectStatus;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectsHomeController {

    private final ScientificProjectService scientificProjectService;

    public ProjectsHomeController(ScientificProjectService scientificProjectService) {
        this.scientificProjectService = scientificProjectService;
    }

    @RequestMapping("/projects")
    String getProjectsHome(){
        return "projects/home";
    }

    @GetMapping({"/", "/scientificprojects"})
    public String listAll(@RequestParam(required = false) String programmeName,
                          @RequestParam(required = false) String grantHolderName,
                          @RequestParam(required = false) Boolean international,
                          @RequestParam(required = false) ScientificProjectStatus status,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String professorName,
                          @RequestParam(required = false) String scientificProjectCallName,
                          @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          Model model) {
        List<ScientificProjectStatus> statuses = List.of(ScientificProjectStatus.values());

        Page<ScientificProject> page = this.scientificProjectService
                .filterAndPaginateJoinedScientificProjects(programmeName, grantHolderName,
                        international, status, name, professorName, scientificProjectCallName,
                        pageNum-1, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("statuses", statuses);
        model.addAttribute("projectstatus", status);
        model.addAttribute("programmeName", programmeName);
        model.addAttribute("grantHolderName", grantHolderName);
        model.addAttribute("international", international);
        model.addAttribute("projectName", name);
        model.addAttribute("professorName", professorName);
        model.addAttribute("projectCallName", scientificProjectCallName);

        return "projects/listProjects";
    }
}

package mk.ukim.finki.wp.fcseservices.web.projects;


import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/scientific-programmes")
@Controller
public class ProjectProgrammesController {

    private final ScientificProjectProgrammeService projectProgrammeService;

    public ProjectProgrammesController(ScientificProjectProgrammeService scientificProjectProgrammeService) {
        this.projectProgrammeService = scientificProjectProgrammeService;
    }

    @RequestMapping()
    String getProgrammeHome(Model model,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer results) {

        Page<ScientificProjectProgramme> projectProgrammes = projectProgrammeService.findAllWithPagination(pageNum, results);
        model.addAttribute("projectProgrammesPage", projectProgrammes);

        return "projects/programme_list";
    }
    @PostMapping("/delete/{id}")
    public String deleteProgramme(@PathVariable(name = "id") Long id) {
        projectProgrammeService.deleteById(id);
        return "redirect:/scientific-programmes";
    }

}

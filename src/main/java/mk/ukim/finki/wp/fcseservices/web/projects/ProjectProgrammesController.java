package mk.ukim.finki.wp.fcseservices.web.projects;


import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectProgrammeNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.service.GrantHolderService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/scientific-programmes")
@Controller
public class ProjectProgrammesController {

    private final ScientificProjectProgrammeService projectProgrammeService;
    private final GrantHolderService grantHolderService;

    public ProjectProgrammesController(ScientificProjectProgrammeService scientificProjectProgrammeService, GrantHolderService grantHolderService) {
        this.projectProgrammeService = scientificProjectProgrammeService;
        this.grantHolderService = grantHolderService;
    }

    @RequestMapping()
    String getProgrammeHome(Model model,
                            @RequestParam(required = false) String errorMessage,
                            @RequestParam(required = false) String successMessage,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer results) {

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        Page<ScientificProjectProgramme> projectProgrammes = projectProgrammeService.findAllWithPagination(pageNum, results);
        model.addAttribute("projectProgrammesPage", projectProgrammes);

        return "projects/programme_list";
    }
    @PostMapping("/delete/{id}")
    public String deleteProgramme(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        projectProgrammeService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Programme deleted");
        return "redirect:/scientific-programmes";
    }

    @GetMapping("/add")
    public String addProgramme(Model model,
                               @RequestParam(required = false) String errorMessage) {

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("grantHolders", grantHolderService.findAll());
        return "projects/form_programme";
    }

    @PostMapping("/add")
    public String addProgramme(Model model,
                               @RequestParam(name = "programmeName") String name,
                               @RequestParam(name = "grantHolderId") Long grantHolderId,
                               @RequestParam(name = "international", defaultValue = "false", required = false) boolean isInternational,
                               RedirectAttributes redirectAttributes
                               ) {

        projectProgrammeService.save(name,grantHolderId,isInternational);
        redirectAttributes.addFlashAttribute("successMessage", "Programme '"+ name +"' added");
        return "redirect:/scientific-programmes";
    }

    @GetMapping("/edit/{id}")
    public String editProgramme(Model model,
                               @PathVariable Long id,
                               @RequestParam(required = false) String errorMessage,
                                RedirectAttributes redirectAttributes) {

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        try {
            model.addAttribute("programme", projectProgrammeService.findById(id).orElseThrow(ScientificProjectProgrammeNotFoundException::new));
            model.addAttribute("grantHolders", grantHolderService.findAll());
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Programme with id '"+ id +"' not found");
            return "redirect:/scientific-programmes";
        }

        return "projects/form_programme";
    }

    @PostMapping("/edit/{id}")
    public String editProgramme(Model model,
                                @PathVariable Long id,
                                @RequestParam(name = "programmeName") String name,
                                @RequestParam(name = "grantHolderId") Long grantHolderId,
                                @RequestParam(name = "international", defaultValue = "false", required = false) boolean isInternational,
                                RedirectAttributes redirectAttributes) {

        projectProgrammeService.edit(id,name,grantHolderId,isInternational);
        redirectAttributes.addFlashAttribute("successMessage", "Programme '"+ name +"' edited");
        return "redirect:/scientific-programmes";
    }

}

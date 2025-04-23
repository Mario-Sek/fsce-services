package mk.ukim.finki.wp.fcseservices.web.projects;

import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectCallNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectProgrammeNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificCallStatus;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;
import mk.ukim.finki.wp.fcseservices.service.GrantHolderService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectCallService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/scientific-project-call")
public class ScientificProjectCallController {
    private final ScientificProjectCallService scientificProjectCallService;
    private final ScientificProjectProgrammeService projectProgrammeService;
    private final GrantHolderService grantHolderService;

    public ScientificProjectCallController(ScientificProjectCallService scientificProjectCallService, ScientificProjectProgrammeService projectProgrammeService, GrantHolderService grantHolderService) {
        this.scientificProjectCallService = scientificProjectCallService;
        this.projectProgrammeService = projectProgrammeService;
        this.grantHolderService = grantHolderService;
    }

    @GetMapping()
    public String listProjects(
            @RequestParam(required = false) Long programmeId,
            @RequestParam(required = false) Long grantHolderId,
            @RequestParam(required = false) Boolean programmeInternational,
            @RequestParam(required = false) ScientificCallStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer results,
            Model model) {

        Page<ScientificProjectCall> page = scientificProjectCallService
                .findAllByPagination(programmeId, grantHolderId, programmeInternational, status, name, pageNum, results);
        model.addAttribute("page", page);
        model.addAttribute("programmes", projectProgrammeService.findAll());
        model.addAttribute("programmeId", programmeId);
        model.addAttribute("grantHolderId", grantHolderId);
        model.addAttribute("grantHolders", grantHolderService.findAll());
        model.addAttribute("programmeInternational", programmeInternational);
        model.addAttribute("status", status);
        model.addAttribute("statuses", ScientificCallStatus.values());
        model.addAttribute("name", name);

        return "/projects/scientific-project-call/list-scientific-project-call";
    }

    @GetMapping("/add-form")
    public String showAdd(Model model) {
        model.addAttribute("grantHolders", grantHolderService.findAll());
        model.addAttribute("programmes", projectProgrammeService.findAll());
        model.addAttribute("statuses", List.of(ScientificCallStatus.values()));
        return "/projects/scientific-project-call/add-project-call";
    }

    @PostMapping("/edit/{id}")
    public String showEdit(Model model, @PathVariable Long id) {
        model.addAttribute("grantHolders", grantHolderService.findAll());
        model.addAttribute("programmes", projectProgrammeService.findAll());
        model.addAttribute("statuses", List.of(ScientificCallStatus.values()));

        ScientificProjectCall projectCall = scientificProjectCallService.findById(id).orElseThrow(ScientificProjectCallNotFoundException::new);

        model.addAttribute("projectCall", projectCall);
        return "/projects/scientific-project-call/add-project-call";
    }

    @PostMapping()
    public String create(@RequestParam String name,
                         @RequestParam LocalDateTime createdAt,
                         @RequestParam LocalDateTime applicationDeadLine,
                         @RequestParam Long programme,
                         @RequestParam ScientificCallStatus status) {

        scientificProjectCallService.save(name, createdAt, applicationDeadLine, programme, status);
        return "redirect:/scientific-project-call";
    }

    @PostMapping("/{id}")
    public String update(@RequestParam Long id,
                         @RequestParam String name,
                         @RequestParam LocalDateTime createdAt,
                         @RequestParam LocalDateTime applicationDeadLine,
                         @RequestParam Long programme,
                         @RequestParam ScientificCallStatus status
    ) {
        scientificProjectCallService.update(id, name, createdAt, applicationDeadLine, programme, status);

        return "redirect:/scientific-project-call";
    }

    @PostMapping("/delete/{id}")
    public String deleteProjectCall(@PathVariable Long id) {
        this.scientificProjectCallService.delete(id);
        return "redirect:/scientific-project-call";
    }

    @PostMapping("/details/{id}")
    public String getProjectDetails(@PathVariable Long id, Model model) {
        var projectCall = scientificProjectCallService.findById(id).orElseThrow(ScientificProjectCallNotFoundException::new);
        model.addAttribute("projectCall", projectCall);
        return "projects/scientific-project-call/details";
    }

}

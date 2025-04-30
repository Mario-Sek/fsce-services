package mk.ukim.finki.wp.fcseservices.web.projects;


import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectStatus;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectCallService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjectsHomeController {

    private final ScientificProjectService scientificProjectService;
    private final ProfessorService professorService;
    private final ScientificProjectCallService scientificProjectCallService;
    private final ScientificProjectProgrammeService projectProgrammeService;

    public ProjectsHomeController(ScientificProjectService scientificProjectService,
                                  ProfessorService professorService,
                                  ScientificProjectCallService scientificProjectCallService,
                                  ScientificProjectProgrammeService projectProgrammeService) {
        this.scientificProjectService = scientificProjectService;
        this.professorService = professorService;
        this.scientificProjectCallService = scientificProjectCallService;
        this.projectProgrammeService = projectProgrammeService;
    }

    @RequestMapping("/projects")
    String getProjectsHome(){
        return "projects/home";
    }

    @GetMapping("/scientific-projects")
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
        model.addAttribute("name", name);
        model.addAttribute("professorName", professorName);
        model.addAttribute("scientificProjectCallName", scientificProjectCallName);

        return "/projects/listProjects";
    }

    @GetMapping("/projects/add")
    public String showAdd(Model model) {
        List<ScientificProjectStatus> statuses = List.of(ScientificProjectStatus.values());
        List<Professor> professors = professorService.findAll();
        List<ScientificProjectCall> projectCalls = scientificProjectCallService.findAll();
        List<ScientificProjectProgramme> programmes = projectProgrammeService.findAll();

        model.addAttribute("statuses", statuses);
        model.addAttribute("professors", professors);
        model.addAttribute("projectCalls", projectCalls);
        model.addAttribute("programmes", programmes);

        return "projects/form";
    }

    @PostMapping("/projects/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        List<ScientificProjectStatus> statuses = List.of(ScientificProjectStatus.values());
        List<Professor> professors = professorService.findAll();
        List<ScientificProjectCall> projectCalls = scientificProjectCallService.findAll();
        List<ScientificProjectProgramme> programmes = projectProgrammeService.findAll();
        ScientificProject project = scientificProjectService.findById(id).orElseThrow(ScientificProjectNotFoundException::new);

        model.addAttribute("statuses", statuses);
        model.addAttribute("professors", professors);
        model.addAttribute("projectCalls", projectCalls);
        model.addAttribute("programmes", programmes);
        model.addAttribute("project", project);

        return "projects/form";
    }

    @PostMapping("/projects")
    public String create(@RequestParam ScientificProjectStatus status,
                         @RequestParam String name,
                         @RequestParam String keywords,
                         @RequestParam String goalsDescription,
                         @RequestParam String relatedPublicationsOrProjects,
                         @RequestParam String report,
                         @RequestParam String expectedResults,
                         @RequestParam Professor coordinator,
                         @RequestParam ScientificProjectCall projectCall,
                         @RequestParam ScientificProjectProgramme programme) {

        this.scientificProjectService.save(status, name, keywords, goalsDescription,
                relatedPublicationsOrProjects, report, expectedResults, coordinator.getId(),
                projectCall.getId(), programme.getId());

        return "redirect:/scientific-projects";
    }

    @PostMapping("/projects/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam ScientificProjectStatus status,
                         @RequestParam String name,
                         @RequestParam String keywords,
                         @RequestParam String goalsDescription,
                         @RequestParam String relatedPublicationsOrProjects,
                         @RequestParam String report,
                         @RequestParam String expectedResults,
                         @RequestParam Professor coordinator,
                         @RequestParam ScientificProjectCall projectCall,
                         @RequestParam ScientificProjectProgramme programme) {

        this.scientificProjectService.edit(id, status, name, keywords, goalsDescription,
                relatedPublicationsOrProjects, report, expectedResults, coordinator.getId(),
                projectCall.getId(), programme.getId());

        return "redirect:/scientific-projects";
    }

    @PostMapping("/projects/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.scientificProjectService.deleteById(id);
        return "redirect:/scientific-projects";
    }

    @GetMapping("/projects/details/{id}")
    public String getProjectDetails(@PathVariable Long id, Model model) {
        ScientificProject project = scientificProjectService.findById(id).orElseThrow(ScientificProjectNotFoundException::new);
        model.addAttribute("project", project);
        return "projects/details";
    }

}

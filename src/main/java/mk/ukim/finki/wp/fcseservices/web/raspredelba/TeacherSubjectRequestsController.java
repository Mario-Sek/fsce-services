package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.config.FacultyUserDetails;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectRequestsDTO;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectRequestsService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject-requests")
public class TeacherSubjectRequestsController {

    private final TeacherSubjectRequestsService teacherSubjectRequestsService;
    private final ProfessorService professorService;
    private final JoinedSubjectService joinedSubjectService;
    private final SemesterManagementService semesterManagementService;
    private final ImportRepository importRepository;

    public TeacherSubjectRequestsController(TeacherSubjectRequestsService teacherSubjectRequestsService,
                                            ProfessorService professorService,
                                            JoinedSubjectService joinedSubjectService,
                                            SemesterManagementService semesterManagementService,
                                            ImportRepository importRepository) {
        this.teacherSubjectRequestsService = teacherSubjectRequestsService;
        this.professorService = professorService;
        this.joinedSubjectService = joinedSubjectService;
        this.semesterManagementService = semesterManagementService;
        this.importRepository = importRepository;
    }

    @GetMapping
    public String getAllTeacherSubjectRequests(Model model,
                                               @RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "20") Integer results,
                                               @RequestParam(required = false) String professorId,
                                               @RequestParam(required = false) String subjectId,
                                               @RequestParam(required = false) SemesterType semesterType) {


        Page<TeacherSubjectRequests> page = teacherSubjectRequestsService.filter(
                professorId,
                subjectId,
                semesterType,
                pageNum,
                results
        );
        model.addAttribute("page", page);

        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);

        List<JoinedSubject> subjects = joinedSubjectService.getAllJoinedSubjects();
        model.addAttribute("subjects", subjects);

        return "raspredelba/teacher-subject-requests/listAllTeacherSubjectRequests";
    }

    @GetMapping("/{professorId}")
    public String getTeacherSubjectRequests(Model model,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "20") Integer results,
                                            @PathVariable("professorId") String professorId) {
        Page<TeacherSubjectRequests> page;
        Professor professor = professorService.findById(professorId);
        page = teacherSubjectRequestsService.filter(
                professorId, null, null, pageNum, results);
        model.addAttribute("page", page);
        model.addAttribute("professorId", professorId);

        return "raspredelba/teacher-subject-requests/listTeacherSubjectRequests";
    }

    @GetMapping("/my")
    public String myRequests(@AuthenticationPrincipal FacultyUserDetails principal) {
        return "redirect:/admin/subject-requests/" + principal.getUsername();
    }

    @GetMapping("/{professorId}/add")
    public String showTeacherSubjectRequestForm(@PathVariable("professorId") String professorId, Model model) {
        model.addAttribute("teacherSubjectRequest", new TeacherSubjectRequests());
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        List<Professor> professors = professorService.getAllProfessors();
        Professor professor = professorService.findById(professorId);
        model.addAttribute("professor", professor);
        model.addAttribute("professors", professors);
        model.addAttribute("semesters", semesters);
        List<JoinedSubject> joinedSubjects = joinedSubjectService.getAllJoinedSubjects();
        model.addAttribute("joinedSubjects", joinedSubjects);
        return "raspredelba/teacher-subject-requests/addTeacherSubjectRequestForm";
    }

    @GetMapping("/{professorId}/edit/{id}")
    public String showEditTeacherSubjectRequestForm(@PathVariable("professorId") String professorId, @PathVariable Long id, Model model, @RequestHeader(value = "Referer", required = false) String referer) {
        TeacherSubjectRequests teacherSubjectRequest = teacherSubjectRequestsService.getTeacherSubjectRequestById(id);
        List<Professor> professors = professorService.getAllProfessors();
        Professor professor = professorService.findById(professorId);
        List<JoinedSubject> subjects = joinedSubjectService.getAllJoinedSubjects();
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        model.addAttribute("professors", professors);
        model.addAttribute("professor", professor);
        model.addAttribute("subjects", subjects);
        model.addAttribute("semesters", semesters);
        model.addAttribute("teacherSubjectRequest", teacherSubjectRequest);
        model.addAttribute("referer", referer);
        return "raspredelba/teacher-subject-requests/editTeacherSubjectRequestForm";
    }

    @PostMapping("/{professorId}/save")
    public String saveTeacherSubjectRequest(@ModelAttribute("teacherSubjectRequest") TeacherSubjectRequests teacherSubjectRequest,
                                            @PathVariable("professorId") String professorId,
                                            @RequestParam("subjectId") String subjectId,
                                            @RequestParam(value = "referer", required = false) String referer) {
        JoinedSubject subject = joinedSubjectService.getByAbbreviation(subjectId);
        teacherSubjectRequest.setSubject(subject);

        Professor professor = professorService.findById(professorId);
        teacherSubjectRequest.setProfessor(professor);

        teacherSubjectRequestsService.saveTeacherSubjectRequest(teacherSubjectRequest);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/subject-requests/" + professorId;
        }
    }

    @GetMapping("/{professorId}/delete/{id}")
    public String deleteTeacherSubjectRequest(@PathVariable("professorId") String professorId, @PathVariable Long id, @RequestHeader(value = "referer", required = false) String referer) {
        teacherSubjectRequestsService.deleteTeacherSubjectRequest(id);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/subject-requests/" + professorId;
        }
    }

    @GetMapping("/display/{id}")
    public String displayTeacherSubjectRequest(@PathVariable Long id, Model model) {
        TeacherSubjectRequests teacherSubjectRequest = teacherSubjectRequestsService.getTeacherSubjectRequestById(id);
        model.addAttribute("teacherSubjectRequest", teacherSubjectRequest);
        return "raspredelba/teacher-subject-requests/displayTeacherSubjectRequest";
    }

    @GetMapping(params = "export")
    public void export(@RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "20") Integer results,
        @RequestParam(required = false) String professorId,
        @RequestParam(required = false) String subjectId,
        @RequestParam(required = false) SemesterType semesterType,
        HttpServletResponse response) {


        Page<TeacherSubjectRequests> page = teacherSubjectRequestsService.filter(
            professorId,
            subjectId,
            semesterType,
            pageNum,
            results
        );

        doExport(response, page.getContent().stream().map(tsr -> new TeacherSubjectRequestsDTO(
            tsr.getProfessor().getId(),
            tsr.getSubject().getAbbreviation(),
            tsr.getSubject().displayName(),
            tsr.getPriority(),
            tsr.getPreferMultipleGroups(),
            tsr.getPreferAuditoriumExercises(),  
            tsr.getPreferLabExercises(),
            tsr.getStartedTeachingFrom().getCode(),
            tsr.getStartedExerciseFrom().getCode(),
            tsr.getAcceptsEnglishGroup()
        )).collect(Collectors.toList()));
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<TeacherSubjectRequestsDTO> example = new ArrayList<>();
        example.add(new TeacherSubjectRequestsDTO("aleksandar.s", "VEB3A", "Web3 апликации", 1.0, true, true, true, "2023-24-S", "2023-24-S", true));
        example.add(new TeacherSubjectRequestsDTO("aleksandar.stojmenski", "AR","Автономна роботика", 1.0, true, false, true, "2023-24-S", "2023-24-S", true));
        example.add(new TeacherSubjectRequestsDTO("aleksandar.tenev", "BNP","Бази на податоци", 1.0, false, false, false, "2023-24-S", "2023-24-S", true));
        doExport(response, example);
    }

    @PostMapping("/import")
    public void importTeacherSubjectRequests(@RequestParam("file") MultipartFile file,
                                     HttpServletResponse response) {

        List<TeacherSubjectRequestsDTO> subjectRequests = importRepository.readPreferences(file, TeacherSubjectRequestsDTO.class);

        List<TeacherSubjectRequestsDTO> importedSubjectRequests = teacherSubjectRequestsService.importTeacherSubjectRequests(subjectRequests);

        String fileName = "imported_subject_request_services.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writePreferences(TeacherSubjectRequestsDTO.class, importedSubjectRequests, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doExport(HttpServletResponse response, List<TeacherSubjectRequestsDTO> data) {
        String fileName = "teacher_subject_requests.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(TeacherSubjectRequestsDTO.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.StudyProgram;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.StudentGroupService;
import mk.ukim.finki.wp.fcseservices.service.StudyProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;

@Controller
@RequestMapping("admin/student-groups")
public class StudentGroupAllocationController {

    private final StudentGroupService studentGroupService;
    private final SemesterManagementService semesterManagementService;
    private final StudyProgramService studyProgramService;

    private final ImportRepository importRepository;

    public StudentGroupAllocationController(StudentGroupService studentGroupService,
                                            SemesterManagementService semesterManagementService,
                                            StudyProgramService studyProgramService, ImportRepository importRepository) {
        this.studentGroupService = studentGroupService;
        this.semesterManagementService = semesterManagementService;
        this.studyProgramService = studyProgramService;
        this.importRepository = importRepository;
    }

    @GetMapping
    public String getStudentGroups(Model model,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "20") Integer results,
                                   @RequestParam(required = false) String semester,
                                   @RequestParam(required = false) Short studyYear,
                                   @RequestParam(required = false) String program) {
        if (semester == null) {
            semester = semesterManagementService.getLastSemester().getCode();
        }
        if (program != null) {
            model.addAttribute("selectedProgram", program);
        }
        if (studyYear != null) {
            model.addAttribute("selectedStudyYear", studyYear);
        }

        Specification<StudentGroup> spec = Specification.where(filterEquals(StudentGroup.class, "semester.code", semester))
                .and(filterContainsText(StudentGroup.class, "programs", program))
                .and(filterEqualsV(StudentGroup.class, "studyYear", studyYear));
        Page<StudentGroup> page = studentGroupService.list(spec, pageNum, results);


        model.addAttribute("semesters", semesterManagementService.getAllSemesters());
        model.addAttribute("programs", studyProgramService.findAll());
        model.addAttribute("page", page);
        model.addAttribute("selectedSemester", semester);

        return "raspredelba/student-groups/listStudentGroups";
    }

    @GetMapping("/add")
    public String addStudentGroup(Model model) {

        List<Semester> semesters = semesterManagementService.getAllSemesters();
        List<StudyProgram> programs = studyProgramService.findAll();

        model.addAttribute("semesters", semesters);
        model.addAttribute("programs", new String[]{});

        return "raspredelba/student-groups/addStudentGroup";
    }

    @GetMapping("/edit/{id}")
    public String editStudentGroup(@PathVariable Long id, Model model) {

        StudentGroup group = this.studentGroupService.getStudentGroupById(id);
        List<Semester> semesters = semesterManagementService.getAllSemesters();
        List<StudyProgram> programs = studyProgramService.findAll();

        model.addAttribute("semesters", semesters);
        model.addAttribute("programs", programs);
        model.addAttribute("group", group);

        return "raspredelba/student-groups/addStudentGroup";
    }

    @PostMapping("/save/")
    public String saveStudentGroup(@RequestParam Short studyYear,
                                   @RequestParam String lastNameRegex,
                                   @RequestParam Semester semester,
                                   @RequestParam String programs,
                                   @RequestParam String name) {
        this.studentGroupService.saveStudentGroup(studyYear, lastNameRegex, semester, programs, name);
        return "redirect:/admin/student-groups";
    }

    @PostMapping("save/{id}")
    public String updateStudentGroup(@PathVariable Long id,
                                     @RequestParam Short studyYear,
                                     @RequestParam String lastNameRegex,
                                     @RequestParam Semester semester,
                                     @RequestParam String programs,
                                     @RequestParam String name) {
        this.studentGroupService.updateStudentGroup(id, studyYear, lastNameRegex, semester, programs, name);
        return "redirect:/admin/student-groups";
    }

    @PostMapping("delete/{id}")
    public String deleteStudentGroup(@PathVariable Long id) {
        this.studentGroupService.deleteStudentGroupById(id);
        return "redirect:/admin/student-groups";
    }

    @PostMapping("cloneSemester")
    public String cloneSemester(@RequestParam String semesterFromCode, @RequestParam String semesterToCode) {
        this.studentGroupService.cloneGroups(semesterFromCode, semesterToCode);

        return "redirect:/admin/student-groups";
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) {

        List<StudentGroup> list = studentGroupService.getAllStudentGroups();

        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"student_groups.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(StudentGroup.class, list, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/import")
    public void importGroups(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<StudentGroup> students = importRepository.readTypeList(file, StudentGroup.class);

        List<StudentGroup> invalidEnrollments = studentGroupService.importGroups(students);
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"student_groups.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(StudentGroup.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
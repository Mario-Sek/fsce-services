package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.dto.StudentDto;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.StudyProgram;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.StudentService;
import mk.ukim.finki.wp.fcseservices.service.StudyProgramService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    private final StudentService studentService;

    private final StudyProgramService studyProgramService;

    private final ImportRepository importRepository;

    public StudentController(StudentService studentService,
                             StudyProgramService studyProgramService,
                             ImportRepository importRepository) {
        this.studentService = studentService;
        this.studyProgramService = studyProgramService;
        this.importRepository = importRepository;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "20") Integer results,
                       @RequestParam(required = false) String nameOrIndex,
                       @RequestParam(name = "studyProgram", required = false) String studyProgramCode) {

        Page<Student> result = studentService.find(pageNum, results, nameOrIndex, studyProgramCode);

        model.addAttribute("page", result);


        List<StudyProgram> studyPrograms = studyProgramService.findAll();
        model.addAttribute("studyPrograms", studyPrograms);

        return "raspredelba/students/list";
    }


    @PostMapping("/import")
    public void importStudents(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<StudentDto> students = importRepository.readTypeList(file, StudentDto.class);

        List<StudentDto> invalidEnrollments = studentService.importStudents(students);

        String fileName = "invalid_students.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(StudentDto.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<StudentDto> example = new ArrayList<>();
        example.add(new StudentDto("111111", "john.doe@students.finki.ukim.mk", "Doe", "John", "Joe", "SIIS23", null));
        example.add(new StudentDto("111111", "jill.doe@students.finki.ukim.mk", "Doe", "Jill", "Joe", "PIT23", null));


        String fileName = "students_example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(StudentDto.class, example, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

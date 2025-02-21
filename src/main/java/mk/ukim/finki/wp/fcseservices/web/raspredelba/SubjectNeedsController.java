package mk.ukim.finki.wp.fcseservices.web.raspredelba;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectNeedsView;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.SubjectNeedsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/subject-needs")
public class SubjectNeedsController {

    private final SubjectNeedsService subjectService;
    private final ImportRepository importRepository;
    private final SemesterManagementService semesterManagementService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "100") Integer results,
                       @RequestParam(required = false) String name,
                       @RequestParam(value = "semester", required = false) String semesterCode,
                       @RequestParam(defaultValue = "false") Boolean lessThanPRG,
                       @RequestParam(defaultValue = "false") Boolean lessThanPR,
                       @RequestParam(defaultValue = "false") Boolean lessThanARG,
                       @RequestParam(defaultValue = "false") Boolean lessThanAR,
                       @RequestParam(defaultValue = "false") Boolean lessThanCL) {

        if (semesterCode == null || semesterCode.isEmpty()) {
            semesterCode = getSemesterCode();
        }

        Page<SubjectNeedsView> page = subjectService.list(semesterCode, name, pageNum, results, lessThanPRG, lessThanPR, lessThanARG, lessThanAR, lessThanCL);
        model.addAttribute("page", page);

        return "raspredelba/subject-needs/list";
    }

    private String getSemesterCode() {
        String semesterCode = "";
        Semester lastActiveSemester = semesterManagementService.getActiveSemester();
        semesterCode = lastActiveSemester.getCode();
        return semesterCode;
    }

    @GetMapping("/export")
    public void sampleTsv(HttpServletResponse response,
                          @RequestParam(required = false) String name,
                          @RequestParam(value = "semester", required = false) String semesterCode,
                          @RequestParam(defaultValue = "false") Boolean lessThanPRG,
                          @RequestParam(defaultValue = "false") Boolean lessThanPR,
                          @RequestParam(defaultValue = "false") Boolean lessThanARG,
                          @RequestParam(defaultValue = "false") Boolean lessThanAR,
                          @RequestParam(defaultValue = "false") Boolean lessThanCL) {

        if (semesterCode == null || semesterCode.isEmpty()) {
            semesterCode = getSemesterCode();
        }

        List<SubjectNeedsView> list = subjectService.findAllForExport(semesterCode, name, lessThanPRG, lessThanPR, lessThanARG, lessThanAR, lessThanCL);

        String fileName = "subject-needs-example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"subject-needs.tsv\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeTypeList(SubjectNeedsView.class, list, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

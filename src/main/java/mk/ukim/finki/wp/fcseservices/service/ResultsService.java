package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ResultsService {

    Optional<Results> findById(Long id);
    Optional<Results> savePdf(Long id, MultipartFile pdfFile) throws IOException;
    Page<Results> filterAndPaginateResultsAll(String professorId, String semesterExamSessionName, List<Course> courses, String examType, String hasResults,
                                              Integer page, Integer size);
    List<Results> filterResultsProfessor(String professorId, List<Course> courses);
    Page<Results> filterAndPaginateResultsStudent(String studentId,String professorId,String yearExamSessionName,String hasResults,String examType,Integer page, Integer size);

    Results uploadResults(Long courseId, String sessionName, MultipartFile pdfFile, String note,String resultType,String profId) throws IOException;
    void initializeForExamSession(String name);

     List<Professor> findProfessorsWithNoResults();

}

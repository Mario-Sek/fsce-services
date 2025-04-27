package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.exceptions.CourseNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ResultNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.YearExamSessionNotFoundException;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.CourseRepository;
import mk.ukim.finki.wp.fcseservices.repository.ResultsRepository;
import mk.ukim.finki.wp.fcseservices.repository.YearExamSessionRepository;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.ResultsService;
import mk.ukim.finki.wp.fcseservices.service.specifications.ResultSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ResultsServiceImpl implements ResultsService {

    private final ResultsRepository resultsRepository;
    private final CourseRepository courseRepository;
    private final YearExamSessionRepository yearExamSessionRepository;
    private final ProfessorService professorService;
    public Optional<Results> findById(Long id) {
        return this.resultsRepository.findById(id);
    }

    public Optional<Results> savePdf(Long id, MultipartFile pdfFile) throws IOException {
        Results result = this.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id));

        byte[] pdfBytes = pdfFile.getBytes();

        result.setPdfBytes(pdfBytes);
        result.setUploadedAt(LocalDateTime.now());

        return Optional.of(this.resultsRepository.save(result));
    }


    public Page<Results> filterAndPaginateResultsAll(String professorId, String semesterExamSessionName, List<Course> courses, String examType, String hasResults,
                                                     Integer page, Integer size) {

        Specification<Results> filters = ResultSpecifications.filterAll(professorId, semesterExamSessionName, courses, hasResults,examType);
        PageRequest pageRequest = PageRequest.of(page -1,size,Sort.by(Sort.Direction.DESC,"uploadedAt"));
        return resultsRepository.findAll(filters, pageRequest);
    }

    public List<Results> filterResultsProfessor(String professorId, List<Course> courses) {

        Specification<Results> filters = ResultSpecifications.filterByProfessor(professorId, courses);
        return resultsRepository.findAll(filters);
    }

    @Override
    public Page<Results> filterAndPaginateResultsStudent(String studentId, String professorId,String yearExamSessionName, String hasResults,String examType,Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page -1,size,Sort.by(Sort.Direction.DESC, "uploadedAt"));
        Specification<Results> filters = ResultSpecifications.filterByStudent(studentId, yearExamSessionName, hasResults,examType,professorId);
        return resultsRepository.findAll(filters, pageRequest);
    }


    public void createResultForExamSessionAndCourseGroup(YearExamSession session, Course course) {

        boolean exists = this.resultsRepository.findBySessionAndJoinedSubject(session,course.getJoinedSubject())
                .isPresent();

        if (!exists) {
            Results results = new Results();
            results.setSession(session);
            results.setJoinedSubject(course.getJoinedSubject());

            this.resultsRepository.save(results);
        }
    }


    public Results uploadResults(Long courseId, String sessionName, MultipartFile pdfFile, String note,String examType,String profId) throws IOException {
        Course course = this.courseRepository.findCourseById(courseId).orElseThrow(() -> new CourseNotFoundException(String
                .format("Course with id %d does not exist", courseId)));
        YearExamSession session = this.yearExamSessionRepository.findByName(sessionName)
                .orElseThrow(() -> new YearExamSessionNotFoundException(String
                        .format("Session with name %s does not exist", sessionName)));


        Professor professor = professorService.findById(profId);
        byte[] pdfBytes = pdfFile.getBytes();

        Results results;
        Optional<Results> resultsOpt = resultsRepository.findByJoinedSubjectAbbreviationAndSessionNameAndResultTypeAndUploadedBy(course.getJoinedSubject().getAbbreviation(),sessionName,examType,professor);

        results = resultsOpt.orElseGet(Results::new);
        results.setUploadedAt(LocalDateTime.now());
        results.setPdfBytes(pdfBytes);
        results.setResultType(examType);
        results.setUploadedBy(professor);
        results.setJoinedSubject(course.getJoinedSubject());
        results.setSession(session);
        results.setNote(note);
        return this.resultsRepository.save(results);

    }

    public void initializeForExamSession(String name) {
        YearExamSession yearExamSession = this.yearExamSessionRepository.findByName(name)
                .orElseThrow(() -> new YearExamSessionNotFoundException(name));

        List<Course> courseGroupList = this.courseRepository
                .findAllBySemesterYear(yearExamSession.getYear());

        courseGroupList
                .forEach(courseGroup -> this.createResultForExamSessionAndCourseGroup(yearExamSession, courseGroup));
    }

    public List<Professor> findProfessorsWithNoResults(){
        return resultsRepository.findProfessorsWithNoResults();
    }

}

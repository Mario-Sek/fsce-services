package mk.ukim.finki.wp.fcseservices.service;



import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface YearExamSessionService {

    Optional<YearExamSession> findByName(String name);

    List<YearExamSession> findAll();

    Page<YearExamSession> findAllWithPaginationAndSorting(Integer page, Integer size, String sortBy);

    void createResultForExamSessionAndCourseGroup(YearExamSession session, Course course);

    void initializeExamSession(String name);
    YearExamSession create(ExamSession session, String year, LocalDate sessionStart, LocalDate sessionEnd, LocalDate enrollmentStartDate, LocalDate enrollmentEndDate, List<StudyCycle> cycle);
    YearExamSession update(String name, ExamSession session, String year, LocalDate sessionStart, LocalDate sessionEnd, LocalDate enrollmentStartDate, LocalDate enrollmentEndDate, List<StudyCycle> cycle);
    YearExamSession delete(String name);
    Page<YearExamSession> findAll(Specification<YearExamSession> filter, Integer page, Integer size);

}

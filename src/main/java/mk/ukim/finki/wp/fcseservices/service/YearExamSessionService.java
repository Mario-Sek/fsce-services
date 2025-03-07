package mk.ukim.finki.wp.fcseservices.service;



import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface YearExamSessionService {

    Optional<YearExamSession> findByName(String name);

    List<YearExamSession> findAll();

    Page<YearExamSession> findAllWithPaginationAndSorting(Integer page, Integer size, String sortBy);

    void createResultForExamSessionAndCourseGroup(YearExamSession session, Course course);

    void initializeExamSession(String name);

}

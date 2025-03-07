package mk.ukim.finki.wp.fcseservices.service.impl;


import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.exceptions.YearExamSessionNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.CourseRepository;
import mk.ukim.finki.wp.fcseservices.repository.ResultsRepository;
import mk.ukim.finki.wp.fcseservices.repository.YearExamSessionRepository;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class YearExamSessionServiceImpl implements YearExamSessionService {
    private final YearExamSessionRepository yearExamSessionRepository;
    private final CourseRepository courseRepository;
    private final ResultsRepository resultsRepository;


    public Optional<YearExamSession> findByName(String name) {
        return this.yearExamSessionRepository.findByName(name);
    }

    public List<YearExamSession> findAll() {
        return yearExamSessionRepository.findAll();
    }

    public Page<YearExamSession> findAllWithPaginationAndSorting(Integer page, Integer size, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, sortBy, Sort.NullHandling.NULLS_LAST));
        Pageable pageable = PageRequest.of(page, size, sort);

        return yearExamSessionRepository.findAll(pageable);
    }

    public void createResultForExamSessionAndCourseGroup(YearExamSession session, Course course) {

        boolean exists = this.resultsRepository.findBySessionAndJoinedSubject(session, course.getJoinedSubject())
                .isPresent();

        if (!exists) {
            Results results = new Results();
            results.setSession(session);
            results.setJoinedSubject(course.getJoinedSubject());

            this.resultsRepository.save(results);
        }
    }

    public void initializeExamSession(String name) {
        YearExamSession semesterExamSession = this.yearExamSessionRepository.findByName(name)
                .orElseThrow(() -> new YearExamSessionNotFoundException(name));

        List<Course> courseGroupList = this.courseRepository
                .findAllBySemesterYear(semesterExamSession.getYear());

        courseGroupList
                .forEach(courseGroup -> this.createResultForExamSessionAndCourseGroup(semesterExamSession, courseGroup));
    }

}

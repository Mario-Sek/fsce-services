package mk.ukim.finki.wp.fcseservices.service.impl;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.exceptions.YearExamSessionNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.YearExamSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;

@AllArgsConstructor
@Service
public class YearExamSessionServiceImpl implements YearExamSessionService {
    private final YearExamSessionRepository yearExamSessionRepository;
    private final CourseRepository courseRepository;
    private final ResultsRepository resultsRepository;
    private final SubjectExamRepository subjectExamRepository;


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

    @Override
    public YearExamSession create(ExamSession session, String year, LocalDate sessionStart, LocalDate sessionEnd, LocalDate enrollmentStartDate, LocalDate enrollmentEndDate, List<StudyCycle> cycle) {
        return this.yearExamSessionRepository.save(new YearExamSession(
                session,
                year,
                sessionStart,
                sessionEnd,
                enrollmentStartDate,
                enrollmentEndDate,
                cycle));
    }

    @Override
    public YearExamSession update(String name, ExamSession session, String year, LocalDate sessionStart, LocalDate sessionEnd, LocalDate enrollmentStartDate, LocalDate enrollmentEndDate, List<StudyCycle> cycle) {
        YearExamSession ses = this.findByName(name).orElseThrow();
        ses.setSession(session);
        ses.setYear(year);
        ses.setSessionStart(sessionStart);
        ses.setSessionEnd(sessionEnd);
        ses.setEnrollmentStartDate(enrollmentStartDate);
        ses.setEnrollmentEndDate(enrollmentEndDate);
        ses.setCycle(cycle);
        return this.yearExamSessionRepository.save(ses);
    }

    @Transactional
    @Override
    public YearExamSession delete(String name) {
        YearExamSession ses = findByName(name).orElseThrow();
        subjectExamRepository.deleteBySession(ses);
        this.yearExamSessionRepository.delete(ses);
        return ses;
    }

    @Override
    public Page<YearExamSession> findAll(Specification<YearExamSession> filter, Integer page, Integer size) {
        return this.yearExamSessionRepository.findAll(filter, PageRequest.of(page - 1, size,
                by("year")
                        .and(by(ASC, "sessionStart"))));
    }

}

package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SubjectAllocationStatsService {

    List<SubjectAllocationStats> getAllSubjectAllocationStats(String semesterCode);

    Page<SubjectAllocationStats> list(String semester,
                                      Integer defaultSemester,
                                      String subject,
                                      Boolean mentorshipCourse,
                                      int page, int results);

    Optional<SubjectAllocationStats> findBySemesterAndSubject(String semester, String subject);

    SubjectAllocationStats editNumberOfGroups(String id, Integer numberOfGroups, Boolean mentorshipCourse);


    void calculate(String semester);

    void deleteById(String id);

    Optional<SubjectAllocationStats> findBySubject(JoinedSubject joinedSubject);

    public Integer getTotalStudents(SubjectAllocationStats subjectAllocationStats);
}

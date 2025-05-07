package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectAllocationStatsRepository extends JpaSpecificationRepository<SubjectAllocationStats, String> {
    Optional<SubjectAllocationStats> findFirstBySubjectAndSemester(JoinedSubject joinedSubject, Semester prevSemester);

    List<SubjectAllocationStats> findBySemesterCode(String semester);

    List<SubjectAllocationStats> findAllBySubject(JoinedSubject subject);
}

package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentGroupRepository extends JpaSpecificationRepository<StudentGroup, Long> {

    List<StudentGroup> findAllBySemester(Semester semester);

    List<StudentGroup> findAllBySemesterCode(String semester);

    Optional<StudentGroup> findFirstBySemesterCodeAndName(String semester, String name);

    List<StudentGroup> findAllByIdIn(List<Long> ids);
}
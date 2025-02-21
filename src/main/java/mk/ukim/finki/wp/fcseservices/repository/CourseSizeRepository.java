package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSizeRepository extends JpaSpecificationRepository<CourseSize, String> {

    List<CourseSize> findAllBySemesterCodeAndJoinedSubjectAbbreviationAndGroupNameIn(String code, String abbreviation, List<String> collect);

    List<CourseSize> findAllBySemesterCodeAndJoinedSubjectAbbreviation(String code, String abbreviation);

    List<CourseSize> findCourseSizesByJoinedSubjectAbbreviation(String abbreviation);

}
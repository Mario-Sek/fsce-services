package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CoursePreferenceRepository extends JpaSpecificationRepository<CoursePreference, String> {

    Page<CoursePreference> findBySubject(JoinedSubject subject, Pageable pageable);

    List<CoursePreference> findCoursePreferencesBySubject_Abbreviation(String abbreviation);
}


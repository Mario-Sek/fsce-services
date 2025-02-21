package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaSpecificationRepository<Course, String> {
    void deleteBySemesterCode(String semesterCode);

    List<Course> findByProfessorsContainingOrAssistantsContaining(String professorId, String assistantId);

    List<Course> findCoursesByJoinedSubject_Abbreviation(String abbreviation);

    List<Course> findCoursesByJoinedSubjectAbbreviationAndSemesterCode(String abbreviation,String semesterCode);


    List<Course> findBySemesterCodeAndProfessorsContainingOrSemesterCodeAndAssistantsContaining(
            String semesterCode,
            String professor,
            String semesterCodeAssistant,
            String assistant
    );

    List<Course> findBySemesterCode(String semester);
}

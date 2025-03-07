package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.StudentCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInCourses extends JpaRepository<StudentCourses, Long> {
    List<StudentCourses> findByStudent(Student student);

}

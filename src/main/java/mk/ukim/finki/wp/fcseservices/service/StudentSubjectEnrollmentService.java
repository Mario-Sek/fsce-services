package mk.ukim.finki.wp.fcseservices.service;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wp.fcseservices.model.dto.SubjectEnrollmentDTO;
import mk.ukim.finki.wp.fcseservices.model.enrollments.StudentSubjectEnrollment;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StudentSubjectEnrollmentService {
    //ne mozhe da e samo import reserved word
    List<SubjectEnrollmentDTO> importStudentSubjects(List<SubjectEnrollmentDTO> importEnrollments, String semester);

    Page<StudentSubjectEnrollment> list(Specification<StudentSubjectEnrollment> spec, int page, int size);

    void deleteBySemester(String semester);

    void allocateGroupsAndSubjects(String semesterCode);

    @Transactional
    void allocateEnrollmentProfessors(String semesterCode);

    @Transactional
    void allocateProfessor(List<Course> subjectCourses);
}

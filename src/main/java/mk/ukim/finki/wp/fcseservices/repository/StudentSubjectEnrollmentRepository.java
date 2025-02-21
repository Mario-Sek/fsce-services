package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.enrollments.StudentSubjectEnrollment;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSubjectEnrollmentRepository extends JpaSpecificationRepository<StudentSubjectEnrollment, String> {
    Page<StudentSubjectEnrollment> findByStudentAndSubjectAndSemester(Student student, Subject subject, Semester semester, Pageable pageable);

    Page<StudentSubjectEnrollment> findByStudent(Student student, Pageable pageable);

    Page<StudentSubjectEnrollment> findBySubject(Subject subject, Pageable pageable);

    Page<StudentSubjectEnrollment> findBySemester(Semester semester, Pageable pageable);

    Page<StudentSubjectEnrollment> findByStudentAndSubject(Student student, Subject subject, Pageable pageable);

    List<StudentSubjectEnrollment> findBySubjectAndSemester(Subject subject, Semester semester);

    List<StudentSubjectEnrollment> findBySemester(Semester semester);

    List<StudentSubjectEnrollment> findByJoinedSubjectAndSemesterOrderByStudentName(JoinedSubject subject, Semester semester);

    Long countBySubjectIdInAndSemesterCodeAndNumEnrollmentsBetween(List<String> codes, String semesterCode, int enrolmentsFrom, int enrollmentsTo);


    @Modifying
    @Query(value = "update student_subject_enrollment sse\n" +
            "set group_id= ?1,\n" +
            "    group_name=?2\n" +
            "FROM student s,\n" +
            "     subject_details sd\n" +
            "where sse.student_student_index = s.student_index\n" +
            "  and sd.id = sse.subject_id\n" +
            "  and ceiling(sd.default_semester / 2.0) = ?4\n" +
            "  and s.study_program_code in ?5\n" +
            "  and s.last_name ~ ?3", nativeQuery = true)
    void updateGroups(Long groupId, String groupName, String lastNamePattern, Short year, List<String> programs);


    @Modifying
    @Query(value = "update student_subject_enrollment sse\n" +
            "set joined_subject_abbreviation=(select js.abbreviation\n" +
            "                                 from joined_subject js\n" +
            "                                 where js.codes like '%' || sse.subject_id || '%'" +
            "                                 limit 1)\n" +
            "where sse.semester_code = ?1", nativeQuery = true)
    void updateJoinedSubjects(@Param("semesterCode") String semesterCode);

    @Modifying
    @Query(value = "update student_subject_enrollment \n" +
            "set professor_id=null\n" +
            "where semester_code = ?1", nativeQuery = true)
    void resetProfessorsInSemester(@Param("semesterCode") String semesterCode);
}

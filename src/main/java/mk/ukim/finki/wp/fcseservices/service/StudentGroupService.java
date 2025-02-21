package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StudentGroupService {

    List<StudentGroup> getAllStudentGroups();

    Page<StudentGroup> list(Specification<StudentGroup> spec, int pageNum, int results);

    StudentGroup getStudentGroupById(Long studentGroupId);

    StudentGroup saveStudentGroup(Short studyYear, String lastNameRegex, Semester semester, String programs, String name);

    StudentGroup saveStudentGroup(StudentGroup studentGroup);

    StudentGroup updateStudentGroup(Long id, Short studyYear, String lastNameRegex, Semester semester, String programs, String name);

    List<StudentGroup> getAllStudentGroupFromSemester(Semester semester);

    void deleteStudentGroupById(Long id);

    List<StudentGroup> importGroups(List<StudentGroup> studentGroups);

    void cloneGroups(String semesterFromCode, String semesterToCode);

    StudentGroup findForStudent(Student student);
}

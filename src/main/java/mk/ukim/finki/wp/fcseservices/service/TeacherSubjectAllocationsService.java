package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectAllocationsDTO;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface TeacherSubjectAllocationsService {


    List<TeacherSubjectAllocations> getAllTeacherSubjectAllocationsBySemester(String semesterCode);

    List<TeacherSubjectAllocations> getAllBySubject(String id);

    void editTeacherSubjectAllocation(TeacherSubjectAllocations editedAllocation);

    TeacherSubjectAllocations addTeacherSubjectAllocation(TeacherSubjectAllocationsDTO newAllocation, String semester);

    void deleteTeacherSubjectAllocations(Long id);

    List<TeacherSubjectAllocations> getAllocationsForSubject(JoinedSubject joinedSubject);

    List<TeacherSubjectAllocations> getAllocationsForSubjectInSemester(JoinedSubject joinedSubject, Semester semester);

    void save(TeacherSubjectAllocations allocation);

    Optional<TeacherSubjectAllocations> findById(Long id);

    void delete(Long id);

    void cloneTeacherSubjectAllocations(String semesterFrom, String semesterTo);

    Page<TeacherSubjectAllocations> findAll(Specification<TeacherSubjectAllocations> filter, Integer pageNum, Integer results);

    void deleteBySemester(String semesterCode);

    boolean validateAllocationsForSemester(String semesterCode);

    List<TeacherSubjectAllocations> importFromList(List<TeacherSubjectAllocations> tsa);

    List<TeacherSubjectAllocations> getTeacherSubjectAllocationsByProfessorId(String professorId);

    List<TeacherSubjectAllocations> getTeacherSubjectAllocationsByProfessorIdAndSemester(String professorId, String semesterCode);

    List<Professor> findProfessorsBySubjectAndSemester(String subjectAbbreviation, String semesterCode);


}

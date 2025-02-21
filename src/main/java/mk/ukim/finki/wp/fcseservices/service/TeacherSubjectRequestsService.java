package mk.ukim.finki.wp.fcseservices.service;



import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectRequestsDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidTeacherSubjectRequestIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherSubjectRequestsService {
    List<TeacherSubjectRequests> getAllTeacherSubjectRequests();

    TeacherSubjectRequests getTeacherSubjectRequestById(Long id) throws InvalidTeacherSubjectRequestIdException;

    TeacherSubjectRequests saveTeacherSubjectRequest(TeacherSubjectRequests teacherSubjectRequest);

    void deleteTeacherSubjectRequest(Long id);

    Page<TeacherSubjectRequests> getTeacherSubjectRequestsByPage(int page, int size);

    Page<TeacherSubjectRequests> filter(String professor,
                                        String subjectId,
                                        SemesterType semesterType,
                                        int page, int size);

    List<TeacherSubjectRequests> getTeacherSubjectRequestsByProfessorId(String professorId);

    List<TeacherSubjectRequests> getTeacherSubjectRequestsBySubject(JoinedSubject subject);

    List<TeacherSubjectRequestsDTO> importTeacherSubjectRequests(List<TeacherSubjectRequestsDTO> teacherSubjectRequests);
}

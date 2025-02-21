package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectDTO;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectProfessorsDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidJoinedSubjectAbbreviationException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface JoinedSubjectService {

    List<JoinedSubject> findAllJoinedSubjects();

    JoinedSubject getByAbbreviation(String abbreviation) throws InvalidJoinedSubjectAbbreviationException;

    JoinedSubject save(JoinedSubject joinedSubject);

    boolean existsByAbbreviation(String abbreviation);

    void updateAbbreviation(String abbreviation, String newAbbreviation);

    void delete(String abbreviation);

    Page<JoinedSubject> filter(String name,
                               String mainSubject,
                               SemesterType semesterType,
                               LocalDate modifiedAfter,
                               int pageNum, int pageSize);

    Page<JoinedSubject> list(int page, int size);

    List<JoinedSubject> getAllJoinedSubjects();

    Page<JoinedSubject> listActivatedSubjects(String name, SemesterType semesterType, int pageNum, int pageSize);

    Page<JoinedSubjectProfessorsDTO> listActivatedSubjectsWithProfessors(String name, SemesterType semesterType, int pageNum, int pageSize);

    List<JoinedSubjectDTO> importJoinedSubjects(List<JoinedSubjectDTO> importSubjects, String semester);
}

package mk.ukim.finki.wp.fcseservices.service.impl;


import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectRequestsDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidTeacherSubjectRequestIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.repository.TeacherSubjectRequestsRepository;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectRequestsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.exceptions.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEqualsV;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class TeacherSubjectRequestsServiceImpl implements TeacherSubjectRequestsService {

    private final TeacherSubjectRequestsRepository teacherSubjectRequestsRepository;
    private final ProfessorService professorService;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final SemesterManagementService semesterManagementService;

    @Override
    public List<TeacherSubjectRequests> getAllTeacherSubjectRequests() {
        return teacherSubjectRequestsRepository.findAll();
    }

    @Override
    public Page<TeacherSubjectRequests> filter(String professor,
                                               String subjectId,
                                               SemesterType semesterType,
                                               int page, int size) {

        Specification<TeacherSubjectRequests> spec = where(filterEquals(TeacherSubjectRequests.class, "professor.id", professor))
                .and(filterEqualsV(TeacherSubjectRequests.class, "subject.semesterType", semesterType))
                .and(filterEquals(TeacherSubjectRequests.class, "subject.abbreviation", subjectId));

        PageRequest pageRequest = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.ASC, "subject.semesterType", "professor.name", "priority"));

        return this.teacherSubjectRequestsRepository.findAll(spec, pageRequest);
    }

    @Override
    public TeacherSubjectRequests getTeacherSubjectRequestById(Long id) {
        return teacherSubjectRequestsRepository.findById(id).orElseThrow(() -> new InvalidTeacherSubjectRequestIdException(id));
    }

    @Override
    public TeacherSubjectRequests saveTeacherSubjectRequest(TeacherSubjectRequests teacherSubjectRequest) {
        return teacherSubjectRequestsRepository.save(teacherSubjectRequest);
    }

    @Override
    public void deleteTeacherSubjectRequest(Long id) {
        teacherSubjectRequestsRepository.deleteById(id);
    }

    @Override
    public Page<TeacherSubjectRequests> getTeacherSubjectRequestsByPage(int page, int size) {
        return teacherSubjectRequestsRepository.findAll(PageRequest.of(page - 1, size,
                Sort.by(Order.asc("professor.id"), Order.asc("priority"))));
    }

    @Override
    public List<TeacherSubjectRequests> getTeacherSubjectRequestsByProfessorId(String professorId) {
        return teacherSubjectRequestsRepository.findByProfessorId(professorId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
    }

    @Override
    public List<TeacherSubjectRequests> getTeacherSubjectRequestsBySubject(JoinedSubject subject) {
        return teacherSubjectRequestsRepository.findBySubject(subject);
    }

    private Optional<TeacherSubjectRequestsDTO> saveTeacherSubjectRequestDTO(TeacherSubjectRequestsDTO dto) {
        try {
            Professor professor = professorService.getProfessorById(dto.getProfessorId());
            JoinedSubject subject = joinedSubjectRepository.findByAbbreviation(dto.getSubjectAbbreviation());
            Semester startedTeachingSemester = semesterManagementService.getSemesterById(dto.getStartedTeachingFromSemesterCode())
            .orElseThrow(SemesterNotFoundException::new);
            Semester startedExerciseSemester = semesterManagementService.getSemesterById(dto.getStartedExerciseFromSemesterCode())
            .orElseThrow(SemesterNotFoundException::new);

            TeacherSubjectRequests teacherSubjectRequest = teacherSubjectRequestsRepository.save(
                new TeacherSubjectRequests(
                    professor,
                    subject,
                    dto.getPriority(),
                    dto.getPreferMultipleGroups(),
                    dto.getPreferAuditoriumExercises(),
                    dto.getPreferLabExercises(),
                    "",
                    "",
                    startedTeachingSemester,
                    startedExerciseSemester,
                    dto.getAcceptsEnglishGroup()
                )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(dto);
    }

    @Override
    public List<TeacherSubjectRequestsDTO> importTeacherSubjectRequests (List<TeacherSubjectRequestsDTO> teacherSubjectRequests) {
        return teacherSubjectRequests.stream()
                .map(dto -> saveTeacherSubjectRequestDTO(dto))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}

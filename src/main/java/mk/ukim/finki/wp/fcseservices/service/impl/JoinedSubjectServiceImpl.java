package mk.ukim.finki.wp.fcseservices.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectDTO;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectProfessorsDTO;
import mk.ukim.finki.wp.fcseservices.model.dto.JoinedSubjectTeacherRequestDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidJoinedSubjectAbbreviationException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.repository.SubjectRepository;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectRequestsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle.TEACHING_ASSISTANT;
import mk.ukim.finki.wp.fcseservices.model.base.TrackedHistoryEntity;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class JoinedSubjectServiceImpl implements JoinedSubjectService {

    private final JoinedSubjectRepository joinedSubjectRepository;
    private final SubjectRepository subjectRepository;

    private final TeacherSubjectRequestsService teacherSubjectRequestsService;

    @Override
    public JoinedSubject findById(String id) {
        return this.joinedSubjectRepository.findByAbbreviation(id);
    }

    @Override
    public List<JoinedSubject> findAllJoinedSubjects() {
        return joinedSubjectRepository.findAll();
    }

    @Override
    public JoinedSubject getByAbbreviation(String abbreviation) {
        return joinedSubjectRepository.findById(abbreviation)
                .orElseThrow(() -> new InvalidJoinedSubjectAbbreviationException(abbreviation));
    }

    @Override
    public JoinedSubject save(JoinedSubject joinedSubject) {
        List<String> history = joinedSubjectRepository.findById(joinedSubject.getAbbreviation())
                .map(TrackedHistoryEntity::getHistoryJson)
                .orElse(new ArrayList<>());
        joinedSubject.setHistoryJson(history);
        return joinedSubjectRepository.save(joinedSubject);
    }

    @Override
    public boolean existsByAbbreviation(String abbreviation) {
        return joinedSubjectRepository.existsById(abbreviation);
    }

    @Override
    @Transactional
    public void updateAbbreviation(String oldAbbreviation, String newAbbreviation) {
        if (existsByAbbreviation(newAbbreviation)) {
            throw new IllegalArgumentException("Joined subject with new ID already exists: " + newAbbreviation);
        }

        JoinedSubject joinedSubject = joinedSubjectRepository.findById(oldAbbreviation)
                .orElseThrow(() -> new InvalidJoinedSubjectAbbreviationException(oldAbbreviation));

        JoinedSubject newJoinedSubject = new JoinedSubject();
        newJoinedSubject.setAbbreviation(newAbbreviation);
        newJoinedSubject.setName(joinedSubject.getName());
        newJoinedSubject.setCodes(joinedSubject.getCodes());
        newJoinedSubject.setSemesterType(joinedSubject.getSemesterType());
        newJoinedSubject.setMainSubject(joinedSubject.getMainSubject());
        newJoinedSubject.setWeeklyLecturesClasses(joinedSubject.getWeeklyLecturesClasses());
        newJoinedSubject.setWeeklyAuditoriumClasses(joinedSubject.getWeeklyAuditoriumClasses());
        newJoinedSubject.setWeeklyLabClasses(joinedSubject.getWeeklyLabClasses());

        joinedSubjectRepository.save(newJoinedSubject);
        joinedSubjectRepository.deleteById(oldAbbreviation);
    }


    @Override
    public void delete(String abbreviation) {
        joinedSubjectRepository.deleteById(abbreviation);
    }

    @Override
    public Page<JoinedSubject> list(int page, int size) {
        return joinedSubjectRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Override
    public List<JoinedSubject> getAllJoinedSubjects() {
        return joinedSubjectRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<JoinedSubject> filter(String name,
                                      String mainSubject,
                                      SemesterType semesterType,
                                      LocalDate modifiedAfter,
                                      int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize,
                Sort.by(Sort.Direction.ASC, "name"));


        Specification<JoinedSubject> spec = where(filterContainsText(JoinedSubject.class, "name", name))
                .and(filterEquals(JoinedSubject.class, "mainSubject.id", mainSubject))
                .and(filterEqualsV(JoinedSubject.class, "semesterType", semesterType))
                .and(greaterThan(JoinedSubject.class, "lastUpdateTime", modifiedAfter));


        return joinedSubjectRepository.findAll(spec, pageRequest);
    }


    @Override
    public Page<JoinedSubject> listActivatedSubjects(String name,
                                                     SemesterType semesterType,
                                                     int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize,
                Sort.by(Sort.Direction.ASC, "name"));

        if (!name.isEmpty() && semesterType != null) {
            return joinedSubjectRepository.findActivatedSubjectsByNameAndSemesterType(pageRequest, name, semesterType.name());
        } else if (!name.isEmpty()) {
            return joinedSubjectRepository.findActivatedSubjectsByName(pageRequest, name);
        } else if (semesterType != null) {
            return joinedSubjectRepository.findActivatedSubjectsBySemesterType(pageRequest, semesterType.name());
        } else {
            return joinedSubjectRepository.findActivatedSubjects(pageRequest);
        }
    }

    @Override
    public Page<JoinedSubjectProfessorsDTO> listActivatedSubjectsWithProfessors(String name, SemesterType semesterType, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize,
                Sort.by(Sort.Direction.ASC, "name"));

        Page<JoinedSubject> subjects = joinedSubjectRepository.findActivatedSubjects(pageRequest);

        if (!name.isEmpty() && semesterType != null) {
            subjects = joinedSubjectRepository.findActivatedSubjectsByNameAndSemesterType(pageRequest, name, semesterType.name());
        } else if (!name.isEmpty()) {
            subjects = joinedSubjectRepository.findActivatedSubjectsByName(pageRequest, name);
        } else if (semesterType != null) {
            subjects = joinedSubjectRepository.findActivatedSubjectsBySemesterType(pageRequest, semesterType.name());
        }

        List<JoinedSubjectTeacherRequestDTO> dtoList = subjects.stream()
                .map(subject -> new JoinedSubjectTeacherRequestDTO(subject,
                        teacherSubjectRequestsService.getTeacherSubjectRequestsBySubject(subject)))
                .collect(Collectors.toList());

        List<JoinedSubjectProfessorsDTO> sorted = dtoList.stream()
                .map(dto -> new JoinedSubjectProfessorsDTO(dto.getSubject(),
                        getSortedTeacherSubjectRequests(dto.getTeacherSubjectRequests())
                                .stream()
                                .map(TeacherSubjectRequests::getProfessor)
                                .collect(Collectors.toList()))).toList();
        Page<JoinedSubjectProfessorsDTO> page = new PageImpl<>(sorted, pageRequest, subjects.getTotalElements());

        return page;
    }

    public List<TeacherSubjectRequests> getSortedTeacherSubjectRequests(List<TeacherSubjectRequests> requests) {
        return requests.stream()
                .sorted(Comparator.comparingInt((TeacherSubjectRequests req) -> {
                            // First compare by how long they are teaching
                            if (req.getProfessor().getTitle().isProfessor() && req.getStartedTeachingFrom()!=null) {
                                return req.getStartedTeachingFrom().yearsPassed();
                            } else if (req.getProfessor().getTitle().equals(TEACHING_ASSISTANT) && req.getStartedExerciseFrom()!=null) {
                                return req.getStartedExerciseFrom().yearsPassed();
                            }
                            return 0;
                        }).reversed()// Sort descending
                        // Then sort them by ordering rank ascending
                        .thenComparingInt(req -> {
                            if (req.getProfessor().getOrderingRank()!=null) {
                                return req.getProfessor().getOrderingRank();
                            }
                            return 1000000;
                        })).toList();
    }

    private Optional<JoinedSubjectDTO> saveJoinedSubject(JoinedSubjectDTO dto, String semester) {
        try {
            Subject subject = subjectRepository.findById(dto.getMainSubjectCode()).get();
            SemesterType semesterType = SemesterType.valueOf(dto.getSemesterType());
            StudyCycle cycle = StudyCycle.valueOf(dto.getCycle());
            JoinedSubject savedJoinedSubject = joinedSubjectRepository.save(
                    new JoinedSubject(
                            dto.getAbbreviation(),
                            dto.getName(),
                            dto.getCodes(),
                            semesterType,
                            subject,
                            dto.getWeeklyLecturesClasses(),
                            dto.getWeeklyAuditoriumClasses(),
                            dto.getWeeklyLabClasses(),
                            cycle,
                            dto.getValidationMessage()
                    )
            );
            return Optional.empty();
        } catch (Exception e) {
            dto.setValidationMessage(e.getMessage());
        }
        return Optional.of(dto);
    }
    @Override
    public List<JoinedSubjectDTO> importJoinedSubjects(List<JoinedSubjectDTO> importSubjects, String semester) {
        return importSubjects.stream()
                .map(dto -> saveJoinedSubject(dto, semester))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Page<JoinedSubject> findPage(Integer page, Integer size, Specification<JoinedSubject> filter) {
        return this.joinedSubjectRepository.findAll(filter, PageRequest.of(page - 1, size,
                by("name")));
    }


}

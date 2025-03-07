package mk.ukim.finki.wp.fcseservices.service.impl;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.dto.TeacherSubjectAllocationsDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidJoinedSubjectAbbreviationException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.SemesterNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.TeacherSubjectAllocationNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@Service
@AllArgsConstructor
public class TeacherSubjectAllocationsServiceImpl implements TeacherSubjectAllocationsService {

    private final TeacherSubjectAllocationsRepository teacherSubjectAllocationsRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final SemesterRepository semesterManagementRepository;
    private final JoinedSubjectService joinedSubjectService;
    private final ProfessorService professorService;
    private final TeacherSubjectAllocationsRepository allocationRepository;
    private final TeacherSubjectRequestsRepository teacherSubjectRequestsRepository;

    private final SemesterManagementService semesterManagementService;
    private final ProfessorRepository professorRepository;
    private final List<TeacherSubjectAllocationValidator> validators;

    @Override
    public List<TeacherSubjectAllocations> getAllTeacherSubjectAllocationsBySemester(String semesterCode) {
        return teacherSubjectAllocationsRepository.findAll().stream()
                .filter(tsa -> tsa.getSemester() != null && tsa.getSemester().getCode().equals(semesterCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectAllocations> getAllBySubject(String id) {
        JoinedSubject subject = joinedSubjectRepository
                .findById(id)
                .orElseThrow(() -> new InvalidJoinedSubjectAbbreviationException(id));
        return teacherSubjectAllocationsRepository.findBySubject(subject, by("subject.name"));
    }

    @Override
    public void editTeacherSubjectAllocation(TeacherSubjectAllocations editedAllocation) {
        TeacherSubjectAllocations originalAllocation = teacherSubjectAllocationsRepository
                .findById(editedAllocation.getId())
                .orElseThrow(() -> new TeacherSubjectAllocationNotFoundException("TeacherSubjectAllocation not found with id: " + editedAllocation.getId()));

        if (editedAllocation.getNumberOfLectureGroups() != null) {
            originalAllocation.setNumberOfLectureGroups(editedAllocation.getNumberOfLectureGroups());
        }
        if (editedAllocation.getNumberOfExerciseGroups() != null) {
            originalAllocation.setNumberOfExerciseGroups(editedAllocation.getNumberOfExerciseGroups());
        }
        if (editedAllocation.getNumberOfLabGroups() != null) {
            originalAllocation.setNumberOfLabGroups(editedAllocation.getNumberOfLabGroups());
        }
        originalAllocation.setEnglishGroup(editedAllocation.getEnglishGroup());

        teacherSubjectAllocationsRepository.save(originalAllocation);
    }

    @Override
    public boolean validateAllocationsForSemester(String semesterCode) {

        if (semesterCode == null || semesterCode.isBlank())
            return false;

        List<TeacherSubjectAllocations> allocations = getAllTeacherSubjectAllocationsBySemester(semesterCode);

        for (TeacherSubjectAllocations allocation : allocations) {

            StringBuilder validationMessage = new StringBuilder();

            List<TeacherSubjectRequests> teacherSubjectRequests = teacherSubjectRequestsRepository
                    .findByProfessorAndSubject(allocation.getProfessor(), allocation.getSubject());

            if (teacherSubjectRequests.isEmpty()) {
                validationMessage.append("No teacher subject request found for the given professor and subject.\n");

            } else {
                for (TeacherSubjectAllocationValidator validator : validators) {
                    String updatedValidationMessage = validator.validateAndUpdateValidationMessage(allocation);
                    validationMessage.append(updatedValidationMessage);
                }
            }
            allocation.setValidationMessage(validationMessage.toString());
            teacherSubjectAllocationsRepository.save(allocation);
        }
        return true;
    }


    @Override
    public TeacherSubjectAllocations addTeacherSubjectAllocation(TeacherSubjectAllocationsDTO newAllocation, String semester) {
        TeacherSubjectAllocations allocation = new TeacherSubjectAllocations();

        Professor professor = professorService.findById(newAllocation.getProfessor());
        JoinedSubject joinedSubject = joinedSubjectService.getByAbbreviation(newAllocation.getSubject());
        Semester sem = semesterManagementService.getSemesterById(semester).orElseThrow(SemesterNotFoundException::new);

        allocation.setProfessor(professor);
        allocation.setSubject(joinedSubject);
        allocation.setNumberOfLectureGroups(newAllocation.getNumberOfLectureGroups());
        allocation.setNumberOfExerciseGroups(newAllocation.getNumberOfExerciseGroups());
        allocation.setNumberOfLabGroups(newAllocation.getNumberOfLabGroups());
        allocation.setSemester(sem);

        return teacherSubjectAllocationsRepository.save(allocation);
    }

    @Override
    public void deleteTeacherSubjectAllocations(Long id) {

        boolean exists = teacherSubjectAllocationsRepository.existsById(id);

        if (exists) {
            teacherSubjectAllocationsRepository.deleteById(id);
        }
    }


    @Override
    public List<TeacherSubjectAllocations> getAllocationsForSubject(JoinedSubject joinedSubject) {
        return allocationRepository.findBySubject(joinedSubject, by(DESC, "semester.startDate")
                .and(by(Sort.Direction.ASC, "professor.id")));
    }

    @Override
    public List<TeacherSubjectAllocations> getAllocationsForSubjectInSemester(JoinedSubject joinedSubject, Semester semester) {
        return allocationRepository.findBySemesterCodeAndSubjectAbbreviationOrderByProfessorOrderingRank(semester.getCode(), joinedSubject.getAbbreviation());
    }

    @Override
    public void save(TeacherSubjectAllocations allocation) {
        allocationRepository.save(allocation);
    }


    @Override
    public Optional<TeacherSubjectAllocations> findById(Long id) {
        return allocationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        this.allocationRepository.deleteById(id);
    }

    @Override
    public void cloneTeacherSubjectAllocations(String semesterFromId, String semesterToId) {
        //find all tsa for that semester
        //create new tsa objects from the prev tsa data -- change semester to semesterTo
        Semester semesterFrom = semesterManagementRepository
                .findById(semesterFromId)
                .orElseThrow(SemesterNotFoundException::new);
        Semester semesterTo = semesterManagementRepository
                .findById(semesterToId)
                .orElseThrow(SemesterNotFoundException::new);

        List<TeacherSubjectAllocations> teacherSubjectAllocationsList = teacherSubjectAllocationsRepository
                .findTeacherSubjectAllocationsBySemester(semesterFrom);
        List<TeacherSubjectAllocations> clonedAllocationsList = teacherSubjectAllocationsList.stream()
                .map(originalAllocation -> {
                    TeacherSubjectAllocations clonedAllocation = new TeacherSubjectAllocations();
                    clonedAllocation.setProfessor(originalAllocation.getProfessor());
                    clonedAllocation.setSubject(originalAllocation.getSubject());
                    clonedAllocation.setEnglishGroup(originalAllocation.getEnglishGroup());
                    clonedAllocation.setNumberOfExerciseGroups(originalAllocation.getNumberOfExerciseGroups());
                    clonedAllocation.setNumberOfLectureGroups(originalAllocation.getNumberOfLectureGroups());
                    clonedAllocation.setNumberOfLabGroups(originalAllocation.getNumberOfLabGroups());
                    clonedAllocation.setSemester(semesterTo);
                    return clonedAllocation;
                })
                .collect(Collectors.toList());

        teacherSubjectAllocationsRepository.saveAll(clonedAllocationsList);
    }

    @Override
    public Page<TeacherSubjectAllocations> findAll(Specification<TeacherSubjectAllocations> filter, Integer pageNum, Integer results) {
        return this.allocationRepository.findAll(filter, PageRequest.of(pageNum - 1, results,
                by("subject.name")
                        .and(by(DESC, "englishGroup", "numberOfLectureGroups", "numberOfExerciseGroups"))));
    }

    @Override
    @Transactional
    public void deleteBySemester(String semesterCode) {
        this.allocationRepository.deleteBySemesterCode(semesterCode);
    }

    @Override
    public List<TeacherSubjectAllocations> importFromList(List<TeacherSubjectAllocations> tsa) {
        return tsa.stream()
                .map(it -> {
                    try {
                        it.setSubject(joinedSubjectRepository.getReferenceById(it.getSubjectId()));
                        it.setProfessor(professorRepository.getReferenceById(it.getProfessorId()));
                        it.setSemester(semesterManagementRepository.getReferenceById(it.getSemesterCode()));
                        teacherSubjectAllocationsRepository.save(it);
                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return it;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectAllocations> getTeacherSubjectAllocationsByProfessorId(String professorId) {
        return teacherSubjectAllocationsRepository.findByProfessorId(professorId);
    }

    @Override
    public List<TeacherSubjectAllocations> getTeacherSubjectAllocationsByProfessorIdAndSemester(String professorId, String semesterCode) {
        return teacherSubjectAllocationsRepository.findByProfessorIdAndSemesterCode(professorId, semesterCode);
    }


    @Override
    public List<Professor> findProfessorsBySubjectAndSemester(String subjectAbbreviation, String semesterCode) {
        return teacherSubjectAllocationsRepository.findProfessorsBySubjectAndSemester(subjectAbbreviation, semesterCode);
    }

}

package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentGroupNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import mk.ukim.finki.wp.fcseservices.repository.SemesterRepository;
import mk.ukim.finki.wp.fcseservices.repository.StudentGroupRepository;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import mk.ukim.finki.wp.fcseservices.service.StudentGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

@AllArgsConstructor
@Service
public class StudentGroupServiceImpl implements StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;

    private final SemesterRepository semesterRepository;

    private final SemesterManagementService semesterManagementService;


    @Override
    public List<StudentGroup> getAllStudentGroups() {
        return this.studentGroupRepository.findAll();
    }

    @Override
    public Page<StudentGroup> list(Specification<StudentGroup> spec, int pageNum, int results) {
        return studentGroupRepository.findAll(spec, of(pageNum - 1, results, by("name")));
    }

    @Override
    public StudentGroup getStudentGroupById(Long studentGroupId) {
        return this.studentGroupRepository.findById(studentGroupId).orElseThrow(() -> new StudentGroupNotFoundException("StudentGroup not found with id: " + studentGroupId));
    }

    @Override
    public StudentGroup saveStudentGroup(Short studyYear, String lastNameRegex, Semester semester, String programs, String name) {
        StudentGroup group = new StudentGroup();
        group.setStudyYear(studyYear);
        group.setLastNameRegex(lastNameRegex);
        group.setPrograms(programs);
        group.setSemester(semester);
        group.setName(name);
        return this.studentGroupRepository.save(group);
    }

    @Override
    public StudentGroup saveStudentGroup(StudentGroup studentGroup) {
        return this.studentGroupRepository.save(studentGroup);
    }

    @Override
    public StudentGroup updateStudentGroup(Long id, Short studyYear, String lastNameRegex, Semester semester, String programs, String name) {
        StudentGroup group = this.getStudentGroupById(id);
        group.setStudyYear(studyYear);
        group.setLastNameRegex(lastNameRegex);
        group.setPrograms(programs);
        group.setSemester(semester);
        group.setName(name);
        return this.studentGroupRepository.save(group);
    }

    @Override
    public List<StudentGroup> getAllStudentGroupFromSemester(Semester semester) {
        return this.studentGroupRepository.findAllBySemester(semester);
    }

    @Override
    public void deleteStudentGroupById(Long id) {
        this.studentGroupRepository.deleteById(id);
    }

    @Override
    public List<StudentGroup> importGroups(List<StudentGroup> studentGroups) {
        return studentGroups.stream()
                .map(dto -> constructAndSaveGroup(dto))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void cloneGroups(String semesterFromCode, String semesterToCode) {
        Semester semesterFrom = this.semesterManagementService.getSemesterById(semesterFromCode).orElse(null);
        Semester semesterTo = this.semesterManagementService.getSemesterById(semesterToCode).orElse(null);

        List<StudentGroup> groups = getAllStudentGroupFromSemester(semesterFrom).stream().map(sourceGroup -> {
            StudentGroup clonedGroup = new StudentGroup();
            clonedGroup.setName(sourceGroup.getName());
            clonedGroup.setStudyYear(sourceGroup.getStudyYear());
            clonedGroup.setLastNameRegex(sourceGroup.getLastNameRegex());
            clonedGroup.setPrograms(sourceGroup.getPrograms());
            clonedGroup.setSemester(semesterTo);
            return clonedGroup;
        }).collect(Collectors.toList());

        studentGroupRepository.saveAll(groups);
    }

    @Override
    public StudentGroup findForStudent(Student student) {
        List<StudentGroup> gruops = this.studentGroupRepository.findAll();
        return null;
    }


    private Optional<StudentGroup> constructAndSaveGroup(StudentGroup dto) {
        try {
            dto.setSemester(semesterRepository.getReferenceById(dto.getSemesterCode()));
            this.studentGroupRepository.save(dto);
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(dto);
    }
}
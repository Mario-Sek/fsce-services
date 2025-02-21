package mk.ukim.finki.wp.fcseservices.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.dto.SubjectEnrollmentDTO;
import mk.ukim.finki.wp.fcseservices.model.enrollments.StudentSubjectEnrollment;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.StudentSubjectEnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class StudentSubjectEnrollmentServiceImpl implements StudentSubjectEnrollmentService {
    private final StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final SubjectAllocationStatsRepository subjectAllocationStatsRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<SubjectEnrollmentDTO> importStudentSubjects(List<SubjectEnrollmentDTO> importEnrollments, String semester) {
        return importEnrollments.stream()
                .map(dto -> saveStudentEnrollment(dto, semester))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    @Override
    public Page<StudentSubjectEnrollment> list(Specification<StudentSubjectEnrollment> spec, int page, int size) {
        return studentSubjectEnrollmentRepository.findAll(spec, PageRequest.of(page - 1, size,
                Sort.by("semester.code", "groupName", "subject.name")));
    }

    private Optional<SubjectEnrollmentDTO> saveStudentEnrollment(SubjectEnrollmentDTO dto, String semester) {
        Student student = studentRepository.getReferenceById(dto.getIndex());
        Subject subject = subjectRepository.getReferenceById(dto.getCode());

        try {
            Semester semesterObj = semesterRepository.findById(semester).orElse(null);

            if (semesterObj != null) {
                studentSubjectEnrollmentRepository.save(new StudentSubjectEnrollment(semesterObj, student, subject, dto.getNumEnrollments().shortValue()));
                return Optional.empty();
            }

        } catch (Exception e) {
            dto.setMessage(e.getMessage());

        }
        return Optional.of(dto);
    }

    public Page<StudentSubjectEnrollment> getEnrollmentsByStudentAndSubjectAndSemester(Student student, Subject subject, Semester semester, int page, int size) {
        return studentSubjectEnrollmentRepository.findByStudentAndSubjectAndSemester(student, subject, semester, PageRequest.of(page - 1, size));
    }


    @Override
    public void deleteBySemester(String semester) {
        List<StudentSubjectEnrollment> enrollmentsToDelete = studentSubjectEnrollmentRepository.findBySemester(semesterRepository.findById(semester).get());
        studentSubjectEnrollmentRepository.deleteAll(enrollmentsToDelete);
    }

    @Transactional
    @Override
    public void allocateGroupsAndSubjects(String semesterCode) {
        List<StudentGroup> groups = studentGroupRepository.findAllBySemesterCode(semesterCode);
        studentSubjectEnrollmentRepository.updateJoinedSubjects(semesterCode);
        groups.forEach(g -> studentSubjectEnrollmentRepository.updateGroups(
                g.getId(),
                g.getName(),
                g.getLastNameRegex(),
                g.getStudyYear(),
                List.of(g.getPrograms().split(";"))
        ));
    }

    @Transactional
    @Override
    public void allocateEnrollmentProfessors(String semesterCode) {
        studentSubjectEnrollmentRepository.resetProfessorsInSemester(semesterCode);
        courseRepository.findBySemesterCode(semesterCode)
                .stream()
                .filter(it -> it.getJoinedSubject() != null)
                .collect(groupingBy(Course::getJoinedSubject))
                .values()
                .forEach(this::allocateProfessor);
    }

    @Transactional
    @Override
    public void allocateProfessor(List<Course> subjectCourses) {
        List<JoinedSubject> subjects = subjectCourses.stream()
                .map(Course::getJoinedSubject)
                .distinct()
                .toList();
        if (subjects.size() != 1) {
            throw new IllegalArgumentException("All courses must be for same subject");
        }

        Boolean hasEnglish = subjectCourses.stream().anyMatch(Course::getEnglish);

        JoinedSubject subject = subjects.get(0);
        List<StudentSubjectEnrollment> enrollments = studentSubjectEnrollmentRepository
                .findByJoinedSubjectAndSemesterOrderByStudentName(
                        subject,
                        subjectCourses.get(0).getSemester()
                );

        Map<Boolean, List<StudentSubjectEnrollment>> groupedByFirst = enrollments.stream()
                .collect(groupingBy(it -> it.getNumEnrollments() == 1 && it.getGroupName() != null));
        List<StudentSubjectEnrollment> reEnrollments = groupedByFirst.getOrDefault(false, new ArrayList<>());
        Map<String, List<StudentSubjectEnrollment>> firstTimeByGroupName = groupedByFirst
                .getOrDefault(true, new ArrayList<>())
                .stream()
                .filter(it -> it.getGroupName() != null)
                .collect(groupingBy(StudentSubjectEnrollment::getGroupName));

        int total = enrollments.size();
        int[] sizes = new int[subjectCourses.size()];
        int courseSize = total / sizes.length;
        List<String> processedGroups = new ArrayList<>();
        for (int i = 0; i < subjectCourses.size(); i++) {
            Course c = subjectCourses.get(i);
            List<String> groups = Stream.of(c.getGroups() != null ? c.getGroups().split(";") : new String[]{}).toList();
            String[] professors = c.getProfessors() != null ? c.getProfessors().split(";") : new String[]{};

            // get the students from the groups
            List<StudentSubjectEnrollment> courseEnrollments = groups.stream()
                    .flatMap(it -> firstTimeByGroupName.getOrDefault(it, List.of()).stream()).toList();

            int portion = courseEnrollments.size() / professors.length;
            // assign enrollment professors
            sizes[i] = courseEnrollments.size();
            setTeachers(c, professors, courseEnrollments, portion);
            // for the english groups, add the re-enrollment students
            if (hasEnglish && c.getEnglish()) {
                // find the english re-enrollments
                List<StudentSubjectEnrollment> enReEnrollments = reEnrollments.stream()
                        .filter(it -> it.getProfessorId() == null
                                && it.getGroupName() != null
                                && it.getGroupName().contains("SEIS"))
                        .toList();

                setTeachers(c, professors, courseEnrollments, portion);
                sizes[i] += enReEnrollments.size();
                // recalculate the average students per group without the english students
                total -= sizes[i];
                if (sizes.length != 1) {
                    courseSize = total / (sizes.length - 1);
                }
            }
            processedGroups.addAll(groups);
        }


        // add the first time students from the groups not assigned to professor
        reEnrollments.addAll(
                firstTimeByGroupName.entrySet()
                        .stream()
                        .filter(it -> !processedGroups.contains(it.getKey()))
                        .flatMap(it -> it.getValue().stream()).toList()
        );

        for (int i = 0; i < subjectCourses.size(); i++) {
            Course c = subjectCourses.get(i);
            // the english re-enrollments has assigned professor
            if (hasEnglish && c.getEnglish()) {
                continue;
            }

            String[] professors = c.getProfessors() != null ? c.getProfessors().split(";") : new String[]{};

            List<StudentSubjectEnrollment> courseEnrollments = reEnrollments.stream()
                    .filter(it -> it.getProfessorId() == null)
                    .limit(Math.max(0, courseSize - sizes[i] + 1))
                    .toList();
            int portion = courseEnrollments.size() / professors.length;

            sizes[i] += courseEnrollments.size();
            setTeachers(c, professors, courseEnrollments, portion);
        }
        studentSubjectEnrollmentRepository.saveAllAndFlush(enrollments);
    }

    private void setTeachers(Course c, String[] professors, List<StudentSubjectEnrollment> courseEnrollments, int portion) {
        for (int j = 0; j < courseEnrollments.size(); j++) {
            courseEnrollments.get(j).setProfessorId(professors[(j / portion) % professors.length]);
            courseEnrollments.get(j).setProfessors(c.getProfessors());
            courseEnrollments.get(j).setAssistants(c.getAssistants());
        }
    }
}

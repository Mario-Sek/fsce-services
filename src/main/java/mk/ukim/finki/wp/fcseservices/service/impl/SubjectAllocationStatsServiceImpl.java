package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import mk.ukim.finki.wp.fcseservices.model.exceptions.SemesterNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.SubjectAllocationStatsNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.SubjectAllocationStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEqualsV;

@Service
@AllArgsConstructor
public class SubjectAllocationStatsServiceImpl implements SubjectAllocationStatsService {

    private final SubjectAllocationStatsRepository subjectAllocationStatsRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final SemesterRepository semesterManagementRepository;
    private final StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
    private final TeacherSubjectAllocationsRepository teacherSubjectAllocationsRepository;


    @Override
    public List<SubjectAllocationStats> getAllSubjectAllocationStats(String semester) {
        return subjectAllocationStatsRepository.findBySemesterCode(semester).stream()
                .sorted(Comparator.comparing(SubjectAllocationStats::getNumberOfGroups).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Page<SubjectAllocationStats> list(String semester,
                                             Integer defaultSemester,
                                             String subject, Boolean mentorshipCourse, int page, int results) {
        Specification<SubjectAllocationStats> spec =
                Specification.where(filterEquals(SubjectAllocationStats.class, "semester.code", semester))
                        .and(filterEquals(SubjectAllocationStats.class, "subject.abbreviation", subject))
                        .and(filterEqualsV(SubjectAllocationStats.class, "mentorshipCourse", mentorshipCourse))
                        .and(filterEqualsV(SubjectAllocationStats.class, "subject.mainSubject.defaultSemester", defaultSemester));
        return subjectAllocationStatsRepository.findAll(spec, PageRequest.of(page - 1, results,
                Sort.by(Sort.Direction.DESC, "numberOfGroups", "numberOfFirstTimeStudents")));
    }

    @Override
    public Optional<SubjectAllocationStats> findBySemesterAndSubject(String semester, String subject) {
        return this.subjectAllocationStatsRepository.findById(SubjectAllocationStats.constructId(semester, subject));
    }

    @Override
    public SubjectAllocationStats editNumberOfGroups(String id, Integer numberOfGroups, Boolean mentorshipCourse) {
        SubjectAllocationStats subjectAllocationStats = subjectAllocationStatsRepository.findById(id)
                .orElseThrow(() -> new SubjectAllocationStatsNotFoundException("Subject allocation stats not found with id: " + id));

        int firstTimeStudents = subjectAllocationStats.getNumberOfFirstTimeStudents() != null ? subjectAllocationStats.getNumberOfFirstTimeStudents() : 0;
        if (firstTimeStudents == 0) {
            // if this value is null, set it to 0
            subjectAllocationStats.setNumberOfFirstTimeStudents(0);
        }
        int reEnrolledStudents = subjectAllocationStats.getNumberOfReEnrollmentStudents() != null ? subjectAllocationStats.getNumberOfReEnrollmentStudents() : 0;
        if (reEnrolledStudents == 0) {
            // if this value is null, set it to 0
            subjectAllocationStats.setNumberOfReEnrollmentStudents(0);
        }

        int totalStudents = firstTimeStudents + reEnrolledStudents;

        subjectAllocationStats.setNumberOfGroups(numberOfGroups);
        if (numberOfGroups != 0) {
            subjectAllocationStats.setAverageStudentsPerGroup(totalStudents / numberOfGroups);
        } else {
            subjectAllocationStats.setAverageStudentsPerGroup(totalStudents);
        }
        subjectAllocationStats.setMentorshipCourse(mentorshipCourse);
        return subjectAllocationStatsRepository.save(subjectAllocationStats);
    }

    @Override
    public void calculate(String semesterCode) {
        Semester semester = semesterManagementRepository.findById(semesterCode)
                .orElseThrow(SemesterNotFoundException::new);
        Semester prevSemester = semesterManagementRepository.findFirstBySemesterTypeAndStartDateLessThanOrderByStartDateDesc(
                        semester.getSemesterType(),
                        semester.getStartDate()
                )
                .orElseThrow(SemesterNotFoundException::new);
        List<SubjectAllocationStats> allocationStats = joinedSubjectRepository
                .findBySemesterType(
                        semester.getSemesterType(),
                        PageRequest.of(0, 1000)
                ).getContent()
                .stream()
                .filter(joinedSubject -> joinedSubject.getCodes() != null)
                .map(joinedSubject -> {
                    SubjectAllocationStats stats = subjectAllocationStatsRepository.findById(SubjectAllocationStats.constructId(semester, joinedSubject))
                            .orElse(new SubjectAllocationStats(semester, joinedSubject));

                    List<String> codes = joinedSubject.codesList();
                    Long firstTimeEnrollments = studentSubjectEnrollmentRepository
                            .countBySubjectIdInAndSemesterCodeAndNumEnrollmentsBetween(
                                    codes,
                                    semesterCode,
                                    -1,
                                    1);
                    Long reEnrollments = studentSubjectEnrollmentRepository
                            .countBySubjectIdInAndSemesterCodeAndNumEnrollmentsBetween(
                                    codes,
                                    semesterCode,
                                    2,
                                    100);

                    stats.setNumberOfFirstTimeStudents(firstTimeEnrollments.intValue());
                    stats.setNumberOfReEnrollmentStudents(reEnrollments.intValue());

                    if (stats.getNumberOfGroups() == 0) {
                        SubjectAllocationStats prevStats = subjectAllocationStatsRepository
                                .findFirstBySubjectAndSemester(joinedSubject, prevSemester)
                                .orElse(new SubjectAllocationStats());
                        stats.setNumberOfGroups(prevStats.getNumberOfGroups());

                        if (prevStats.getNumberOfGroups() == 0) {
                            Float totalGroups = teacherSubjectAllocationsRepository.getCoveredLectureGroupsInSemester(
                                    joinedSubject.getAbbreviation(),
                                    prevSemester.getCode()
                            );
                            if (totalGroups != null) {
                                stats.setNumberOfGroups(totalGroups.intValue());
                            }
                        }
                    }

                    stats.setCoveredLectureGroups(teacherSubjectAllocationsRepository.getCoveredLectureGroupsInSemester(
                            joinedSubject.getAbbreviation(),
                            semester.getCode()
                    ));
                    stats.setCoveredExerciseGroups(teacherSubjectAllocationsRepository.getCoveredExerciseGroupsInSemester(
                            joinedSubject.getAbbreviation(),
                            semester.getCode()
                    ));
                    stats.setCoveredLabGroups(teacherSubjectAllocationsRepository.getCoveredLabGroupsInSemester(
                            joinedSubject.getAbbreviation(),
                            semester.getCode()
                    ));


                    stats.setCalculatedNumberOfGroups((int) Math.ceil(firstTimeEnrollments.doubleValue() / 100));
                    if (stats.getNumberOfGroups() >= 1) {
                        stats.setAverageStudentsPerGroup(stats.getTotalStudents() / stats.getNumberOfGroups());
                    }

                    return stats;
                }).collect(Collectors.toList());

        subjectAllocationStatsRepository.saveAll(allocationStats);

    }

    @Override
    public void deleteById(String id) {
        subjectAllocationStatsRepository.deleteById(id);
    }


}

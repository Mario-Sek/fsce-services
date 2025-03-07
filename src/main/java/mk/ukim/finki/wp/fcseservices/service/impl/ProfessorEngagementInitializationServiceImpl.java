package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorEngagementAlreadyInitializedForSemester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.ProfessorEngagementInitializationService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProfessorEngagementInitializationServiceImpl implements ProfessorEngagementInitializationService {

    private final ProfessorEngagementRepository engagementRepository;
    private final SemesterRepository semesterManagementRepository;
    private final TeacherSubjectAllocationsRepository allocationsRepository;
    private final CourseRepository courseRepository;
    private final CoursePreferenceRepository preferenceRepository;
    private final SubjectAllocationStatsRepository subjectAllocationStatsRepository;
    private final ProfessorService professorService;


    @Override
    public void initSemester(String semesterCode) {

        Long count = engagementRepository.countBySemester_Code(semesterCode);
        if (count > 0) {
            throw new ProfessorEngagementAlreadyInitializedForSemester(semesterCode);
        }
        Semester semester = semesterManagementRepository.getReferenceById(semesterCode);
        Map<JoinedSubject, List<CoursePreference>> prefBySubject = preferenceRepository.findAll()
                .stream().collect(Collectors.groupingBy(CoursePreference::getSubject));
        Map<JoinedSubject, List<SubjectAllocationStats>> statsBySubject = subjectAllocationStatsRepository
                .findBySemesterCode(semesterCode)
                .stream().collect(Collectors.groupingBy(SubjectAllocationStats::getSubject));
        List<Course> groups = courseRepository.findBySemesterCode(semesterCode);
        List<TeacherSubjectAllocations> allocations = allocationsRepository.findBySemesterCode(semesterCode);

        List<ProfessorEngagement> engagements = new ArrayList<>();

        for (Course group : groups) {
            String[] professors = group.getProfessors().split(";");
            String[] assistants = group.getAssistants().split(";");
            boolean sharedLecture = professors.length > 1;
            boolean sharedExercise = assistants.length > 1;
            boolean isEnglish = group.getGroups().contains("SEIS") && !group.getGroups().contains(";");
            CoursePreference pref = prefBySubject.getOrDefault(group.getJoinedSubject(), List.of(new CoursePreference())).get(0);
            SubjectAllocationStats stat = statsBySubject.get(group.getJoinedSubject()).get(0);
            boolean sharedStudents = LectureSharing.TOPICS.equals(pref.getLectureSharing());

            for (String p : professors) {
                if (p.trim().isEmpty()) {
                    continue;
                }
                Professor prof = professorService.findById(p);
                if (sharedLecture) {
                    Double groupsNum = this.countGroups(allocations, p, group, isEnglish, ClassType.Lecture);
                    Float calculatedNumberOfClasses = (float) (group.getJoinedSubject().getWeeklyLecturesClasses() * groupsNum);
                    Short calculatedNumberOfStudents = (short) (stat.getAverageStudentsPerGroup().shortValue() * groupsNum);
                    engagements.add(new ProfessorEngagement(
                            prof,
                            semester,
                            group.getJoinedSubject(),
                            ClassType.Lecture,
                            isEnglish ? Language.English : Language.Macedonian,
                            calculatedNumberOfClasses,
                            true,
                            calculatedNumberOfStudents,
                            false,
                            "Shared",
                            calculatedNumberOfClasses,
                            calculatedNumberOfStudents)
                    );
                } else {
                    Float calculatedNumberOfClasses = group.getJoinedSubject().getWeeklyLecturesClasses().floatValue();
                    Short calculatedNumberOfStudents = sharedStudents ? stat.getAverageStudentsPerGroup().shortValue() : group.getTotalStudents().shortValue();
                    engagements.add(new ProfessorEngagement(
                            prof,
                            semester,
                            group.getJoinedSubject(),
                            ClassType.Lecture,
                            isEnglish ? Language.English : Language.Macedonian,
                            calculatedNumberOfClasses,
                            false,
                            calculatedNumberOfStudents,
                            false,
                            null,
                            calculatedNumberOfClasses,
                            calculatedNumberOfStudents)
                    );
                }
            }

            for (String p : assistants) {
                if (p.trim().isEmpty()) {
                    continue;
                }
                Professor prof = professorService.findById(p);
                if (sharedExercise) {
                    Double groupsNum = this.countGroups(allocations, p, group, isEnglish, ClassType.Auditory_Exercises);
                    Float calculatedNumberOfClasses = (float) (group.getJoinedSubject().getWeeklyAuditoriumClasses() * groupsNum);
                    Short calculatedNumberOfStudents = (short) (stat.getAverageStudentsPerGroup().shortValue() * groupsNum);
                    engagements.add(new ProfessorEngagement(
                            prof,
                            semester,
                            group.getJoinedSubject(),
                            ClassType.Auditory_Exercises,
                            isEnglish ? Language.English : Language.Macedonian,
                            calculatedNumberOfClasses,
                            true,
                            calculatedNumberOfStudents,
                            false,
                            "Shared",
                            calculatedNumberOfClasses,
                            calculatedNumberOfStudents)
                    );
                } else {
                    Float calculatedNumberOfClasses = group.getJoinedSubject().getWeeklyAuditoriumClasses().floatValue();
                    Short calculatedNumberOfStudents = sharedStudents ? stat.getAverageStudentsPerGroup().shortValue() : group.getTotalStudents().shortValue();
                    engagements.add(new ProfessorEngagement(
                            prof,
                            semester,
                            group.getJoinedSubject(),
                            ClassType.Auditory_Exercises,
                            isEnglish ? Language.English : Language.Macedonian,
                            calculatedNumberOfClasses,
                            false,
                            calculatedNumberOfStudents,
                            false,
                            null,
                            calculatedNumberOfClasses,
                            calculatedNumberOfStudents)
                    );
                }
                Double groupsNum = this.countGroups(allocations, p, group, isEnglish, ClassType.Laboratory_Exercises);
                Float calculatedNumberOfClasses = (float) (group.getJoinedSubject().getWeeklyLabClasses() * groupsNum);
                Short calculatedNumberOfStudents = pref.isLabExercisesAsConsultations() ?
                        (short) (stat.getAverageStudentsPerGroup().shortValue() * groupsNum) :
                        (short) (groupsNum * 20);
                engagements.add(new ProfessorEngagement(
                        prof,
                        semester,
                        group.getJoinedSubject(),
                        ClassType.Laboratory_Exercises,
                        isEnglish ? Language.English : Language.Macedonian,
                        calculatedNumberOfClasses,
                        false,
                        calculatedNumberOfStudents,
                        pref.isLabExercisesAsConsultations(),
                        null,
                        calculatedNumberOfClasses,
                        calculatedNumberOfStudents)
                );
            }


        }

        this.engagementRepository.saveAll(engagements);
    }

    private Double countGroups(List<TeacherSubjectAllocations> allocations,
                               String professor,
                               Course group,
                               boolean isEnglish,
                               ClassType type) {
        return allocations.stream().filter(it -> it.getProfessor().getId().equals(professor)
                && it.getSubject().getAbbreviation().equals(group.getJoinedSubject().getAbbreviation())
                && it.getEnglishGroup() == isEnglish
                && (ClassType.Lecture.equals(type) && it.getNumberOfLectureGroups() > 0
                || ClassType.Auditory_Exercises.equals(type) && it.getNumberOfExerciseGroups() > 0
                || ClassType.Laboratory_Exercises.equals(type) && it.getNumberOfLabGroups() > 0)
        ).mapToDouble(it -> switch (type) {
            case Lecture -> it.getNumberOfLectureGroups();
            case Auditory_Exercises -> it.getNumberOfExerciseGroups();
            default -> it.getNumberOfLabGroups();
        }).sum();

    }


}

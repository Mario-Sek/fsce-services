package mk.ukim.finki.wp.fcseservices.service.impl;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.dto.CourseDto;
import mk.ukim.finki.wp.fcseservices.model.exceptions.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectAllocationStats;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.StudentGroup;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;

@AllArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final CourseSizeRepository sizeRepository;
    private final SubjectAllocationStatsRepository subjectAllocationStatsRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final TeacherSubjectAllocationsRepository teacherSubjectAllocationsRepository;
    private final RoomRepository roomRepository;
    private final ProfessorRepository professorRepository;
    private final SemesterRepository semesterManagementRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final CoursePreferenceRepository coursePreferenceRepository;

    @Override
    public Course findCourseById(String id) throws CourseNotFoundException {
        return this.repository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found"));
    }

    @Override
    public List<Course> findAllCourses() {
        return this.repository.findAll();
    }

    /**
     * Builds groups and assigns teachers for all subjects in a given semester.
     *
     * @param semesterCode Semester code.
     */
    @Transactional
    @Override
    public void calculateGroups(String semesterCode) {
        repository.deleteBySemesterCode(semesterCode);

        List<CourseSize> allGroups = sizeRepository.findAll(filterEquals(CourseSize.class, "semesterCode", semesterCode));
        List<TeacherSubjectAllocations> allTeacherAllocations = teacherSubjectAllocationsRepository.findBySemesterCode(semesterCode);
        List<SubjectAllocationStats> subjectAllocations = subjectAllocationStatsRepository.findAll(
                filterEquals(SubjectAllocationStats.class, "semester.code", semesterCode)
                        .and(greaterThan(SubjectAllocationStats.class, "numberOfGroups", 0))
                        .and(filterEqualsV(SubjectAllocationStats.class, "mentorshipCourse", false))
        );

        subjectAllocations.forEach(sa -> calculateGroupsForSubject(sa, allGroups, allTeacherAllocations));
    }

    public List<Course> calculateGroupsForSubject(SubjectAllocationStats sa, List<CourseSize> allGroups,
                                                  List<TeacherSubjectAllocations> allTeacherAllocations) {
        Integer numberOfGroups = sa.getNumberOfGroups();
        if (numberOfGroups == 0) {
            return new ArrayList<>();
        }

        List<CourseSize> subjectGroups = allGroups != null ?
                allGroups.stream()
                        .filter(g -> sa.getSubject().getAbbreviation().equals(g.getJoinedSubjectAbbreviation()))
                        .collect(toList()) :
                sizeRepository.findAllBySemesterCodeAndJoinedSubjectAbbreviation(
                        sa.getSemester().getCode(),
                        sa.getSubject().getAbbreviation()
                );
        List<TeacherSubjectAllocations> teachers = allTeacherAllocations != null ?
                allTeacherAllocations.stream()
                        .filter(it -> it.getSubject().getAbbreviation().equals(sa.getSubject().getAbbreviation()))
                        .collect(toList()) :
                teacherSubjectAllocationsRepository.findBySemesterCodeAndSubjectAbbreviation(
                        sa.getSemester().getCode(),
                        sa.getSubject().getAbbreviation()
                );

        boolean isEnglish = teachers.stream().anyMatch(t -> t.getEnglishGroup() != null && t.getEnglishGroup());
        List<List<CourseSize>> combinedGroups = combineGroups(numberOfGroups, subjectGroups, isEnglish);
        List<List<List<TeacherSubjectAllocations>>> combinedTeachers = combineTeachers(numberOfGroups, teachers, combinedGroups);
        List<List<TeacherSubjectAllocations>> professors = combinedTeachers.get(0);
        List<List<TeacherSubjectAllocations>> assistants = combinedTeachers.get(1);

        List<Course> groups = new ArrayList<>();
        for (int i = 0; i < numberOfGroups; i++) {
            List<CourseSize> combinedGroup = combinedGroups.get(i);
            List<TeacherSubjectAllocations> professor = professors.get(i);
            List<TeacherSubjectAllocations> assistant = assistants.get(i);
            Course group = createGroup(sa, combinedGroup, professor, assistant);
            groups.add(group);
        }

        return groups;
    }

    @Override
    public Page<Course> list(String semesterCode, String subject, String groupName, String professor, Integer pageNum, Integer results) {
        Specification<Course> spec = Specification
                .where(filterEquals(Course.class, "semester.code", semesterCode))
                .and(filterEquals(Course.class, "joinedSubject.abbreviation", subject))
                .and(filterContainsText(Course.class, "groups", groupName))
                .and(filterContainsText(Course.class, "professors", professor))
                .or(filterContainsText(Course.class, "assistants", professor));
        return repository.findAll(spec, PageRequest.of(pageNum - 1, results,
                Sort.by("semester.code", "joinedSubject.name", "professors")));
    }

    @Override
    public List<CourseDto> importData(List<CourseDto> data) {
        return data.stream().map(this::saveCourseDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<CourseDto> saveCourseDto(CourseDto dto) {
        try {
            Course instance = new Course();
            instance.setSemester(semesterManagementRepository.findById(dto.getSemesterCode()).orElseThrow(SemesterNotFoundException::new));
            instance.setJoinedSubject(joinedSubjectRepository.findById(dto.getJoinedSubject()).orElseThrow(InvalidCourseException::new));
            instance.setProfessors(dto.getProfessors());
            instance.setAssistants(dto.getAssistants());
            setGroups(instance, dto.getGroups());
            calculateNumberOfStudents(instance, dto.getGroups());
            repository.save(instance);
            return Optional.empty();
        } catch (Exception e) {
            dto.setMessage(e.getMessage());
        }
        return Optional.of(dto);
    }

    @Override
    public void save(String semesterCode, String subject, String professors, String assistants, String groups) {
        Course instance = new Course();
        instance.setSemester(semesterManagementRepository.findById(semesterCode).orElseThrow(SemesterNotFoundException::new));
        instance.setJoinedSubject(joinedSubjectRepository.findById(subject).orElseThrow(InvalidCourseException::new));
        instance.setProfessors(professors);
        instance.setAssistants(assistants);
        setGroups(instance, groups);
        calculateNumberOfStudents(instance, groups);
        repository.save(instance);
    }

    @Override
    public void createAndCalculate(String semesterCode, String subject) {
        String statsId = SubjectAllocationStats.constructId(semesterCode, subject);
        SubjectAllocationStats stats = subjectAllocationStatsRepository.findById(statsId)
                .orElseThrow(InvalidSubjectAllocationStatsException::new);
        calculateGroupsForSubject(stats, null, null);
    }

    @Override
    public List<Course> getCoursesByProfessorId(String professorId) {
        return repository.findByProfessorsContainingOrAssistantsContaining(professorId, professorId);
    }

    @Override
    public List<Course> getCoursesBySubject(String abbreviation) {
        return repository.findCoursesByJoinedSubject_Abbreviation(abbreviation);
    }

    @Override
    public List<Course> getCoursesBySubjectAndSemester(String abbreviation, String semesterCode) {
        return repository.findCoursesByJoinedSubjectAbbreviationAndSemesterCode(abbreviation, semesterCode);
    }

    @Override
    public List<Course> getCoursesByProfessorIdAndSemester(String professorId, String assistantId, String semesterCode) {
        return repository.findBySemesterCodeAndProfessorsContainingOrSemesterCodeAndAssistantsContaining(semesterCode, professorId, semesterCode, assistantId);
    }

    @Override
    public void save(String id, String professors, String assistants, String groups, Boolean english) {
        Course instance = repository.findById(id).orElseThrow(InvalidCourseException::new);
        instance.setProfessors(professors);
        instance.setAssistants(assistants);
        if (groups != null) {
            setGroups(instance, groups);
            calculateNumberOfStudents(instance, groups);
            instance.setRooms(roomRepository.findByCapacityGreaterThanOrderByCapacityAsc(
                    instance.getNumberOfFirstEnrollments().longValue() - 20));
        }
        instance.setEnglish(english);
        repository.save(instance);
    }

    private void setGroups(Course instance, String groups) {
        List<StudentGroup> validatedGroups = Stream.of(groups.split(";"))
                .map(String::trim)
                .map(it -> studentGroupRepository.findFirstBySemesterCodeAndName(instance.getSemester().getCode(), it))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        instance.setGroups(validatedGroups.stream().map(StudentGroup::getName)
                .collect(joining(";")));
    }

    private void calculateNumberOfStudents(Course instance, String groups) {

        List<CourseSize> sizes = sizeRepository.findAllBySemesterCodeAndJoinedSubjectAbbreviationAndGroupNameIn(instance.getSemester().getCode(),
                instance.getJoinedSubject().getAbbreviation(),
                Stream.of(groups.split(";")).map(String::trim).collect(toList()));
        instance.setNumberOfFirstEnrollments(sizes.stream().mapToInt(it -> it.getFirstTimeEnrollments().intValue())
                .sum());
        instance.setNumberOfReEnrollments(sizes.stream().mapToInt(it -> it.getReEnrollments().intValue())
                .sum());
    }

    /**
     * Assigns professors and assistants to each group.
     *
     * @param numberOfGroups Number of groups for a given subject. User should provide a positive integer upon invocation.
     * @param teachers       Teachers (professors and/or assistants).
     * @param courseSizeList Student groups.
     * @return A list containing a list of all professor allocations for all groups as first element, and a list of all assistant allocations for all groups as second element.
     */
    public List<List<List<TeacherSubjectAllocations>>> combineTeachers(int numberOfGroups,
                                                                       List<TeacherSubjectAllocations> teachers,
                                                                       List<List<CourseSize>> courseSizeList) {
        Function<TeacherSubjectAllocations, Integer> isProfessorAssistant =
                t -> t.getNumberOfLectureGroups() > 0F && t.getNumberOfExerciseGroups() > 0F ? 1 : 0;
        teachers = teachers.stream()
                .sorted(Comparator.comparing(isProfessorAssistant).reversed())
                .toList();
        float[] lectureSizes = new float[numberOfGroups];
        float[] exerciseSizes = new float[numberOfGroups];
        Boolean[] englishFlags = courseSizeList.stream()
                .map(l -> l.stream().anyMatch(cs -> cs.getEnglish() != null && cs.getEnglish()))
                .toArray(Boolean[]::new);
        List<List<List<TeacherSubjectAllocations>>> groups = IntStream.range(0, 2)
                .mapToObj(i -> IntStream.range(0, numberOfGroups)
                        .mapToObj(j -> (List<TeacherSubjectAllocations>) new ArrayList<TeacherSubjectAllocations>())
                        .toList())
                .toList();

        Arrays.fill(lectureSizes, 0);
        Arrays.fill(exerciseSizes, 0);
        bestFitTeachers(teachers, lectureSizes, exerciseSizes, englishFlags, groups);
        return groups;
    }

    /**
     * Assigns teachers to all groups.
     *
     * @param teachers      Teachers (professors and/or assistants).
     * @param lectureSizes  Number of lectures allocated for each group.
     * @param exerciseSizes Number of exercises allocated for each group.
     * @param englishFlags  Descriptor for english groups.
     * @param groups        Teacher allocations per groups.
     * @return A list of teachers who could not be allocated (can be removed).
     */
    private List<TeacherSubjectAllocations> bestFitTeachers(List<TeacherSubjectAllocations> teachers,
                                                            float[] lectureSizes, float[] exerciseSizes,
                                                            Boolean[] englishFlags,
                                                            List<List<List<TeacherSubjectAllocations>>> groups) {
        List<TeacherSubjectAllocations> noRoom = new ArrayList<>();

        List<CoursePreference> preference = coursePreferenceRepository.findCoursePreferencesBySubject_Abbreviation(
                teachers.get(0).getSubject().getAbbreviation()
        );
        boolean lectureSharing = preference.get(0).getLectureSharing() == LectureSharing.TOPICS;
        boolean exerciseSharing = preference.get(0).getAuditoriumExercisesSharing() == LectureSharing.TOPICS;

        int numberOfProfessors = (int) teachers.stream()
                .filter(teacher -> teacher.getNumberOfLectureGroups() > 0)
                .count();
        int numberOfProfessorsEN = (int) teachers.stream()
                .filter(teacher -> teacher.getNumberOfLectureGroups() > 0 && teacher.getEnglishGroup() != null && teacher.getEnglishGroup())
                .count();
        int numberOfAssistants = (int) teachers.stream()
                .filter(teacher -> teacher.getNumberOfExerciseGroups() > 0)
                .count();
        int numberOfAssistantsEN = (int) teachers.stream()
                .filter(teacher -> teacher.getNumberOfExerciseGroups() > 0 && teacher.getEnglishGroup() != null && teacher.getEnglishGroup())
                .count();

        for (TeacherSubjectAllocations t : teachers) {
            Float tLectures = t.getNumberOfLectureGroups();
            Float tExercises = t.getNumberOfExerciseGroups();
            boolean canTeachEnglish = t.getEnglishGroup() != null && t.getEnglishGroup();

            boolean[] allocatedLGroups = new boolean[lectureSizes.length];
            boolean[] allocatedEGroups = new boolean[exerciseSizes.length];
            Arrays.fill(allocatedLGroups, false);
            Arrays.fill(allocatedEGroups, false);

            while (tLectures > 0F || tExercises > 0F) {
                float minValue = 100;
                float lectureValue = -1;
                float exerciseValue = -1;
                int bestIndex = -1;

                for (int i = 0; i < lectureSizes.length; ++i) {
                    if ((!canTeachEnglish && englishFlags[i]) ||
                            (lectureSharing && allocatedLGroups[i]) ||
                            (exerciseSharing && allocatedEGroups[i]))
                        continue;

                    float lValue = lectureSharing ?
                            Math.min(tLectures, 1f / (englishFlags[i] ? numberOfProfessorsEN : numberOfProfessors)) :
                            Math.min(tLectures, 1);
                    float eValue = exerciseSharing ?
                            Math.min(tExercises, 1f / (englishFlags[i] ? numberOfAssistantsEN : numberOfAssistants)) :
                            Math.min(tExercises, 1);

                    if (lectureSizes[i] + lValue <= 1 && exerciseSizes[i] + eValue <= 1) {
                        float value = (1 - lectureSizes[i] - lValue) + (1 - exerciseSizes[i] - eValue);
                        if (minValue > value) {
                            minValue = value;
                            lectureValue = lValue;
                            exerciseValue = eValue;
                            bestIndex = i;
                        }
                    }
                }

                if (bestIndex >= 0) {
                    if (lectureValue > 0) {
                        lectureSizes[bestIndex] += lectureValue;
                        groups.get(0).get(bestIndex).add(t);
                        tLectures -= Math.min(lectureValue, 1);
                        allocatedLGroups[bestIndex] = true;
                    }
                    if (exerciseValue > 0) {
                        exerciseSizes[bestIndex] += exerciseValue;
                        groups.get(1).get(bestIndex).add(t);
                        tExercises -= Math.min(exerciseValue, 1);
                        allocatedEGroups[bestIndex] = true;
                    }
                } else {
                    noRoom.add(t);
                    break;
                }
            }
        }

        return noRoom;
    }

    public List<List<CourseSize>> combineGroups(int numberOfGroups, List<CourseSize> subjectGroups, boolean isEnglish) {
        if (numberOfGroups == 0) {
            return new ArrayList<>();
        }

        subjectGroups = subjectGroups.stream()
                .filter(it -> it.getFirstTimeEnrollments() > 14)
                .sorted(Comparator.comparing(CourseSize::getFirstTimeEnrollments))
                .collect(toList());
        int studentsPerGroup = subjectGroups.stream()
                .mapToInt(it -> it.getFirstTimeEnrollments().intValue())
                .sum() / numberOfGroups;
        List<List<CourseSize>> groups = IntStream.range(0, numberOfGroups)
                .mapToObj(i -> (List<CourseSize>) new ArrayList<CourseSize>())
                .collect(toList());
        int numberOfGroupsMkd = numberOfGroups;

        if (isEnglish) {
            List<CourseSize> subjectGroupsEn = subjectGroups.stream()
                    .filter(g -> g.getEnglish() != null && g.getEnglish())
                    .toList();
            int numberOfGroupsEn = Math.min(
                    (int) Math.ceil(subjectGroupsEn.stream()
                            .mapToDouble(CourseSize::getFirstTimeEnrollments)
                            .sum() / studentsPerGroup),
                    subjectGroupsEn.size()
            );
            numberOfGroupsMkd -= numberOfGroupsEn;

            int[] sizesEn = new int[numberOfGroupsEn];
            List<CourseSize> noRoom = bestFit(subjectGroupsEn, sizesEn, studentsPerGroup, groups, false);
            bestFit(noRoom, sizesEn, studentsPerGroup, groups, true);

            groups.sort(Comparator.comparing(List::size));  // push allocated english groups to the end of list
        }

        int[] sizes = new int[numberOfGroupsMkd];
        List<CourseSize> subjectGroupsMkd = subjectGroups.stream()
                .filter(group -> group.getEnglish() == null || !group.getEnglish())
                .toList();

        // best fit :)
        // <3
        List<CourseSize> noRoom = bestFit(subjectGroupsMkd, sizes, studentsPerGroup, groups, false);
        bestFit(noRoom, sizes, studentsPerGroup, groups, true);

        groups.sort(Comparator.comparing(g -> g.stream().noneMatch(gg -> gg.getEnglish() != null && gg.getEnglish())));
        return groups;
    }

    private Course createGroup(SubjectAllocationStats subjectStats, List<CourseSize> subjectGroups,
                               List<TeacherSubjectAllocations> professors, List<TeacherSubjectAllocations> assistants) {
        Course group = new Course();
        group.setSemester(subjectStats.getSemester());
        group.setJoinedSubject(subjectStats.getSubject());
        group.setGroups(subjectGroups.stream().map(CourseSize::getGroupName)
                .collect(joining(";")));
        group.setNumberOfFirstEnrollments((int) subjectGroups.stream().mapToLong(CourseSize::getFirstTimeEnrollments).sum());
        group.setNumberOfReEnrollments((int) subjectGroups.stream().mapToLong(CourseSize::getReEnrollments).sum());
        group.setEnglish(subjectGroups.stream().anyMatch(it -> it.getEnglish() != null && it.getEnglish()));
        // set professor
        group.setProfessors(professors.stream().map(it -> it.getProfessor().getId()).collect(joining(";")));

        // set assistant
        group.setAssistants(assistants.stream().map(it -> it.getProfessor().getId()).collect(joining(";")));

        //  set rooms
        group.setRooms(
                roomRepository.findByCapacityGreaterThanOrderByCapacityAsc(group.getNumberOfFirstEnrollments().longValue() - 20)
        );

        repository.save(group);
        return group;
    }

    private List<CourseSize> bestFit(List<CourseSize> subjectGroups, int[] sizes, int studentsPerGroup,
                                     List<List<CourseSize>> groups, boolean allowOverflow) {
        List<CourseSize> noRoom = new ArrayList<>();

        for (int i = subjectGroups.size() - 1; i >= 0; i--) {
            CourseSize t = subjectGroups.get(i);
            int minDiff = 1000;
            int bestIx = -1;

            for (int j = 0; j < sizes.length; j++) {
                int diff = studentsPerGroup - (sizes[j] + t.getFirstTimeEnrollments().intValue());
                if (allowOverflow) {
                    diff = Math.abs(diff);
                }
                if (diff < minDiff && diff >= 0) {
                    minDiff = diff;
                    bestIx = j;
                }
            }
            if (bestIx >= 0) {
                sizes[bestIx] += t.getFirstTimeEnrollments();
                groups.get(bestIx).add(t);
            } else {
                noRoom.add(t);
            }
        }
        return noRoom;
    }

    @Override
    public String toTsv(List<Course> groups) {
        StringBuilder sb = new StringBuilder();
        groups.forEach(g -> {
            if (g.getProfessors().equals(g.getAssistants())) {
                processGroup(sb, g, g.getJoinedSubject().getWeeklyLecturesClasses()
                        + g.getJoinedSubject().getWeeklyAuditoriumClasses(), g.getProfessors());
            } else {
                processGroup(sb, g, g.getJoinedSubject().getWeeklyLecturesClasses(), g.getProfessors());
                processGroup(sb, g, g.getJoinedSubject().getWeeklyAuditoriumClasses(), g.getAssistants());
            }
        });
        return sb.toString();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    public void processGroup(StringBuilder sb, Course g, Integer classes, String professors) {
        sb.append(g.getJoinedSubject().getName()).append("\t");
        sb.append(g.getGroups().replaceAll(";", ",")).append("\t");

        sb.append(classes).append("\t");

        sb.append(Stream.of(professors.split(";"))
                .map(professorRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).map(Professor::getName)
                .collect(joining(","))).append("\t");

        sb.append(g.getRooms().stream().map(Room::getName).collect(joining(","))).append("\t");
        sb.append("\n");
    }
}

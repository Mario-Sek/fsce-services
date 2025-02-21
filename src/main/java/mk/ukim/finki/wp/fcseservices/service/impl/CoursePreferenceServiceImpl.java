package mk.ukim.finki.wp.fcseservices.service.impl;


import mk.ukim.finki.wp.fcseservices.model.dto.CoursePreferenceDTO;
import mk.ukim.finki.wp.fcseservices.model.base.TrackedHistoryEntity;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidCoursePreferenceIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import mk.ukim.finki.wp.fcseservices.repository.CoursePreferenceRepository;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.service.CoursePreferenceService;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoursePreferenceServiceImpl implements CoursePreferenceService {

    private final CoursePreferenceRepository coursePreferenceRepository;
    private final JoinedSubjectService subjectService;
    private final JoinedSubjectRepository joinedSubjectRepository;

    @Autowired
    public CoursePreferenceServiceImpl(CoursePreferenceRepository coursePreferenceRepository,
                                       JoinedSubjectService subjectService, JoinedSubjectRepository joinedSubjectRepository) {
        this.coursePreferenceRepository = coursePreferenceRepository;
        this.subjectService = subjectService;
        this.joinedSubjectRepository = joinedSubjectRepository;

    }

    @Override
    public CoursePreference getCoursePreferenceById(String id) {
        return coursePreferenceRepository.findById(id)
                .orElseThrow(() -> new InvalidCoursePreferenceIdException(id));
    }

    @Override
    public CoursePreference saveCoursePreference(JoinedSubject subject,
                                                 LectureSharing lectureSharing,
                                                 LectureSharing auditoriumExercisesSharing,
                                                 boolean preferOnlineLectures,
                                                 boolean preferOnlineExercises,
                                                 boolean labExercisesAsConsultations) {

        CoursePreference coursePreference = coursePreferenceRepository.findById(subject.getAbbreviation())
                .orElse(new CoursePreference(subject));

        List<String> history = coursePreferenceRepository.findById(subject.getAbbreviation())
                .map(TrackedHistoryEntity::getHistoryJson)
                .orElse(new ArrayList<>());
        coursePreference.setHistoryJson(history);

        coursePreference.setLectureSharing(lectureSharing);
        coursePreference.setAuditoriumExercisesSharing(auditoriumExercisesSharing);
        coursePreference.setPreferOnlineLectures(preferOnlineLectures);
        coursePreference.setPreferOnlineExercises(preferOnlineExercises);
        coursePreference.setLabExercisesAsConsultations(labExercisesAsConsultations);


        return coursePreferenceRepository.save(coursePreference);
    }

    @Override
    public CoursePreference save(CoursePreference preference) {
        return this.coursePreferenceRepository.save(preference);
    }


    private Optional<CoursePreferenceDTO> saveCoursePreference(CoursePreferenceDTO dto, String semester) {
        try {
            JoinedSubject subject = joinedSubjectRepository.findByName(dto.getName());

            String id = subject.getAbbreviation();
            CoursePreference savedPreference = coursePreferenceRepository.save(
                    new CoursePreference(
                            id,
                            subject,
                            LectureSharing.valueOf(dto.getLectureSharing()),
                            LectureSharing.valueOf(dto.getAuditoriumExercisesSharing()),
                            dto.isPreferOnlineLectures(),
                            dto.isPreferOnlineExercises(),
                            dto.isLabExercisesAsConsultations()
                    )
            );
            return Optional.empty();
        } catch (Exception e) {
            dto.setMessage(e.getMessage());
        }
        return Optional.of(dto);
    }


    @Override
    public void deleteCoursePreference(String id) {
        coursePreferenceRepository.deleteById(id);
    }


    @Override
    public List<CoursePreference> getAllCoursePreferences() {
        return coursePreferenceRepository.findAll();
    }

    @Override
    public Page<CoursePreference> filterAndPaginateCoursePreferences(String subjectId, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);

        if (subjectId != null && !subjectId.isEmpty()) {
            JoinedSubject subject = subjectService.getByAbbreviation(subjectId);
            return coursePreferenceRepository.findBySubject(subject, pageRequest);
        } else {
            return coursePreferenceRepository.findAll(pageRequest);
        }
    }

    @Override
    public List<CoursePreference> getCoursePreferencesBySubject(String abbreviation) {
        return coursePreferenceRepository.findCoursePreferencesBySubject_Abbreviation(abbreviation);
    }

    @Override
    public Page<CoursePreference> list(Specification<CoursePreference> spec, int page, int size) {
        return coursePreferenceRepository.findAll(spec, PageRequest.of(page - 1, size));
    }

    @Override
    public List<CoursePreferenceDTO> importCoursePreferences(List<CoursePreferenceDTO> importPreferences, String semester) {
        return importPreferences.stream()
                .map(dto -> saveCoursePreference(dto, semester))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


}

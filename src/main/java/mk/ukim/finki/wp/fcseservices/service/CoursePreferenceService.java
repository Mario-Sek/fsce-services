package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.dto.CoursePreferenceDTO;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidCoursePreferenceIdException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CoursePreferenceService {
    List<CoursePreferenceDTO> importCoursePreferences(List<CoursePreferenceDTO> importPreferences, String semester);

    CoursePreference getCoursePreferenceById(String id) throws InvalidCoursePreferenceIdException;

    CoursePreference saveCoursePreference(JoinedSubject subject,
                                          LectureSharing lectureSharing,
                                          LectureSharing auditoriumExercisesSharing,
                                          boolean preferOnlineLectures,
                                          boolean preferOnlineExercises,
                                          boolean labExercisesAsConsultations);

    CoursePreference save(CoursePreference preference);

    void deleteCoursePreference(String id);


    List<CoursePreference> getAllCoursePreferences();


    Page<CoursePreference> filterAndPaginateCoursePreferences(String subjectId, int pageNum, int results);

    List<CoursePreference> getCoursePreferencesBySubject(String abbreviation);

    Page<CoursePreference> list(Specification<CoursePreference> spec, int page, int size);
}


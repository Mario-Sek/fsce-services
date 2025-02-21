package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseSizeService {

    Page<CourseSize> list(String semester, String joinedSubject, Long groupId, Integer pageNum, Integer results);

    List<CourseSize> getCourseSizesBySubject(String abbreviation);


    List<CourseSize> getCourseSizesBySubjectAndSemester(String abbreviation, String semesterCode);
}

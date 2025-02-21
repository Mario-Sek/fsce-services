package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.exceptions.CourseNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.model.dto.CourseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    Course findCourseById(String id) throws CourseNotFoundException;
    List<Course> findAllCourses();

    void calculateGroups(String semesterCode);

    Page<Course> list(String semesterCode, String subject, String groupName, String professor, Integer pageNum, Integer results);

    List<CourseDto> importData(List<CourseDto> students);

    void save(String id, String professors, String assistants, String groups, Boolean english);

    String toTsv(List<Course> groups);

    void delete(String id);

    void save(String semesterCode, String subject, String professors, String assistants, String groups);

    void createAndCalculate(String semesterCode, String subject);

    List<Course> getCoursesByProfessorId(String professorId);

    List<Course> getCoursesBySubject(String abbreviation);

    List<Course> getCoursesBySubjectAndSemester(String abbreviation, String semesterCode);

    List<Course> getCoursesByProfessorIdAndSemester(String professorId, String assistantId, String semesterCode);
}

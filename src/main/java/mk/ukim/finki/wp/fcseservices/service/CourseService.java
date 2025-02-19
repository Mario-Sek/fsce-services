package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.exceptions.CourseNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;

import java.util.List;

public interface CourseService {

    Course findCourseById(String id) throws CourseNotFoundException;
    List<Course> findAllCourses();
}

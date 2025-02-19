package mk.ukim.finki.wp.fcseservices.service.impl;


import mk.ukim.finki.wp.fcseservices.model.exceptions.CourseNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.CourseRepository;
import mk.ukim.finki.wp.fcseservices.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course findCourseById(String id) throws CourseNotFoundException {
        return this.courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found"));
    }

    @Override
    public List<Course> findAllCourses() {
        return this.courseRepository.findAll();
    }
}

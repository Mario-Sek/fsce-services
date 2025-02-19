package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.CourseRepository;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryRecordRepository;
import mk.ukim.finki.wp.fcseservices.repository.StudentRepository;
import mk.ukim.finki.wp.fcseservices.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final DisciplinaryRecordRepository reportRepository;
    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    public StudentServiceImpl(DisciplinaryRecordRepository reportRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.reportRepository = reportRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Student findStudentByIndex(String index) throws StudentNotFoundException {
        return this.studentRepository.findById(index).orElseThrow(() -> new StudentNotFoundException("Student cannot be found"));
    }

    @Override
    public List<Student> findAllStudents() {
        return this.studentRepository.findAll();
    }

    @Override
    public List<Student> findStudentByStatus(DisciplinaryStatus status) {
        return Collections.emptyList();
    }

}

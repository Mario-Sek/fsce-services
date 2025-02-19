package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;

import java.util.List;

public interface StudentService {

    Student findStudentByIndex(String index) throws StudentNotFoundException;
    List<Student> findAllStudents();
    List<Student> findStudentByStatus(DisciplinaryStatus status);
}

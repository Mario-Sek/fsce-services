package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.dto.StudentDto;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    Student findStudentByIndex(String index) throws StudentNotFoundException;
    List<Student> findAllStudents();
    List<Student> findStudentByStatus(DisciplinaryStatus status);

    Page<Student> find(Integer page, Integer size, String nameOrIndex, String studyProgramCode);

    List<StudentDto> importStudents(List<StudentDto> students);
}

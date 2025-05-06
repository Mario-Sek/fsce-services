package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.dto.StudentDto;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.CourseRepository;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryRecordRepository;
import mk.ukim.finki.wp.fcseservices.repository.StudentRepository;
import mk.ukim.finki.wp.fcseservices.repository.StudyProgramRepository;
import mk.ukim.finki.wp.fcseservices.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final DisciplinaryRecordRepository disciplinaryRecordRepository;


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
        return disciplinaryRecordRepository.findAllByStatus(status)
                .stream()
                .map(DisciplinaryRecord::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Page<Student> find(Integer page, Integer size, String nameOrIndex, String studyProgramCode) {
        return this.studentRepository.findAll(PageRequest.of(page - 1, size));
    }

    @Override
    public List<StudentDto> importStudents(List<StudentDto> students) {
        return students.stream()
                .map(dto -> saveStudent(dto))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<StudentDto> saveStudent(StudentDto dto) {
        try {
            Student student = new Student(dto.getIndex(),
                    dto.getEmail(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getParentName(),
                    studyProgramRepository.getReferenceById(dto.getStudyProgramCode())
            );
            this.studentRepository.save(student);
            return Optional.empty();
        } catch (Exception e) {
            dto.setMessage(e.getMessage());
        }
        return Optional.of(dto);
    }

}

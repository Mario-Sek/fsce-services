package mk.ukim.finki.wp.fcseservices.repository;



import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaryRecordRepository extends JpaRepository<DisciplinaryRecord, String> {

    List<DisciplinaryRecord> findAllByStudent(Student student);

    List<DisciplinaryRecord> findAllByMeetingIsNull();
}

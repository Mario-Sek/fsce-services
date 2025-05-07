package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaryRecordRepository extends JpaSpecificationRepository<DisciplinaryRecord, String> {

    List<DisciplinaryRecord> findAllByStudent(Student student);

    List<DisciplinaryRecord> findAllByMeetingIsNull();

    List<DisciplinaryRecord> findAllByMeeting(DisciplinaryMeeting meeting);

    List<DisciplinaryRecord> findAllByStatus(DisciplinaryStatus status);
}

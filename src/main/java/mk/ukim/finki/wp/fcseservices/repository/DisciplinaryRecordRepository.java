package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaryRecordRepository extends JpaSpecificationRepository<DisciplinaryRecord, String> {

    List<DisciplinaryRecord> findAllByStudent(Student student);

    List<DisciplinaryRecord> findAllByMeetingIsNull();

    @Query("SELECT DISTINCT dr.student FROM DisciplinaryRecord dr WHERE dr.status = :status")
    List<Student> findDistinctStudentsByStatus(@Param("status") DisciplinaryStatus status);

}

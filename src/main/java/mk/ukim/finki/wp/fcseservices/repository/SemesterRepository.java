package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterState;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {
    Semester findFirstByOrderByCodeDesc();

    Optional<Semester> findFirstBySemesterTypeAndStartDateLessThanOrderByStartDateDesc(
            SemesterType semesterType,
            LocalDate startDate);

    Optional<Semester> findFirstByStateIn(List<SemesterState> states);

    @Query("SELECT s FROM Semester s WHERE s.code = ?1 AND s.state != 'INACTIVE'")
    List<Semester> findActiveSemesterByCode(String semesterCode);
}

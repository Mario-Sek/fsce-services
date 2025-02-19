package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaryMeetingRepository extends JpaRepository<DisciplinaryMeeting, Long> {
}

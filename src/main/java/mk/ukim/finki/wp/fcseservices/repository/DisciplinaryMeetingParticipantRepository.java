package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeeting;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaryMeetingParticipantRepository extends JpaRepository<DisciplinaryMeetingParticipant, Long> {
    List<DisciplinaryMeetingParticipant> findAllByMeeting(DisciplinaryMeeting meeting);

    void deleteAllByMeeting(DisciplinaryMeeting disciplinaryMeeting);
}

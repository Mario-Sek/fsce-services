package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeeting;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryMeetingNotFound;
import mk.ukim.finki.wp.fcseservices.model.exceptions.JoinedSubjectNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface MeetingService {
    DisciplinaryMeeting createNewMeeting(String meetingDate);

    void addParticipants(String participants, DisciplinaryMeeting meeting) throws ProfessorNotFoundException, StudentNotFoundException;

    List<DisciplinaryMeeting> getAllMeetings();

    DisciplinaryMeeting getMeetingById(Long id) throws DisciplinaryMeetingNotFound;

    String getParticipantsForMeeting(DisciplinaryMeeting meeting);

    DisciplinaryMeeting editMeeting(Long id, String meetingDate, List<String> professorIds) throws ProfessorNotFoundException;

    Page<DisciplinaryMeeting> findAllMeetings(String professor, LocalDate date, Long recordId, int pageNumber, int pageSize) throws ProfessorNotFoundException;

    List<DisciplinaryMeeting> getAllUniqueMeetingsByDate();


}

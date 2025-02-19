package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeeting;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryMeetingNotFound;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;

import java.util.List;

public interface MeetingService {
    DisciplinaryMeeting createNewMeeting(String meetingDate);

    void addParticipants(String participants, DisciplinaryMeeting meeting) throws ProfessorNotFoundException, StudentNotFoundException;

    List<DisciplinaryMeeting> getAllMeetings();

    DisciplinaryMeeting getMeetingById(Long id) throws DisciplinaryMeetingNotFound;

    String getParticipantsForMeeting(DisciplinaryMeeting meeting);

    DisciplinaryMeeting editMeeting(Long id, String meetingDate, List<String> professorIds, List<String> studentsIds) throws ProfessorNotFoundException, StudentNotFoundException;
}

package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeeting;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryMeetingParticipant;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorRepository;
import mk.ukim.finki.wp.fcseservices.service.MeetingService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final DisciplinaryMeetingRepository meetingRepository;
    private final DisciplinaryRecordRepository recordRepository;
    private final ProfessorRepository professorRepository;
    private final DisciplinaryMeetingParticipantRepository participantRepository;
    private final StudentRepository studentRepository;

    @Override
    public DisciplinaryMeeting createNewMeeting(String meetingDate) {
        LocalDate date = LocalDate.parse(meetingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DisciplinaryMeeting disciplinaryMeeting = new DisciplinaryMeeting();
        disciplinaryMeeting.setDisciplinaryMeetingDate(date);

        DisciplinaryMeeting savedMeeting = meetingRepository.save(disciplinaryMeeting);

        List<DisciplinaryRecord> records = recordRepository.findAllByMeetingIsNull().stream()
                .filter(record -> record.getStatus() == DisciplinaryStatus.REPORTED)
                .toList();

        for (DisciplinaryRecord record : records) {
            record.setMeeting(savedMeeting);
            record.setStatus(DisciplinaryStatus.SCHEDULED);
        }

        recordRepository.saveAll(records);

        return savedMeeting;
    }


    @Override
    public void addParticipants(String participants, DisciplinaryMeeting meeting) throws ProfessorNotFoundException, StudentNotFoundException {
        List<DisciplinaryMeetingParticipant> participantList = new ArrayList<>();
        String[] participantIds = participants.split(",");

        for (String participantId : participantIds) {
            DisciplinaryMeetingParticipant participant = new DisciplinaryMeetingParticipant();
            participant.setMeeting(meeting);

            Professor professor = professorRepository.findById(participantId)
                    .orElseThrow(() -> new ProfessorNotFoundException("Professor not found"));
            participant.setProfessor(professor);

            participantList.add(participant);
        }

        participantRepository.saveAll(participantList);
    }


    @Override
    public List<DisciplinaryMeeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public DisciplinaryMeeting getMeetingById(Long id) {
        return meetingRepository.findById(id).orElseThrow();
    }

    @Override
    public String getParticipantsForMeeting(DisciplinaryMeeting meeting) {
        List<DisciplinaryMeetingParticipant> participants = participantRepository.findAllByMeeting(meeting);
        return participants.stream().map(participant -> participant.getProfessor().getId())
                .collect(Collectors.joining(", "));
    }

    @Override
    public DisciplinaryMeeting editMeeting(Long id, String meetingDate, List<String> professorIds)
            throws ProfessorNotFoundException {
        DisciplinaryMeeting disciplinaryMeeting = meetingRepository.findById(id).orElseThrow();
        LocalDate date = LocalDate.parse(meetingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        disciplinaryMeeting.setDisciplinaryMeetingDate(date);

        participantRepository.deleteAllByMeeting(disciplinaryMeeting);

        List<DisciplinaryMeetingParticipant> participantList = new ArrayList<>();


        for (String professorId : professorIds) {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new ProfessorNotFoundException("Professor not found"));
            DisciplinaryMeetingParticipant participant = new DisciplinaryMeetingParticipant();
            participant.setMeeting(disciplinaryMeeting);
            participant.setProfessor(professor);
            participantList.add(participant);
        }

        participantRepository.saveAll(participantList);
        return meetingRepository.save(disciplinaryMeeting);
    }

    @Override
    public Page<DisciplinaryMeeting> findAllMeetings(String professor, LocalDate date, Long recordId, int pageNumber, int pageSize) throws ProfessorNotFoundException {
        Professor professor1;
        if (professor != null && !professor.isEmpty())
            professor1 = professorRepository.findById(professor)
                    .orElseThrow(() -> new ProfessorNotFoundException("Professor cannot be found"));
        else {
            professor1 = null;
        }

        DisciplinaryRecord record1;
        if (recordId != null)
            record1 = recordRepository.findById(String.valueOf(recordId)).orElseThrow(() -> new RuntimeException("Record cannot be found"));
        else {
            record1 = null;
        }

        LocalDate date1 = date;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "disciplinaryMeetingDate"));

        Page<DisciplinaryMeeting> allMeetings = meetingRepository.findAll(pageable);

        if(professor1 == null && date1 == null && record1 == null)
            return new PageImpl<>(allMeetings.stream().toList());

        List<DisciplinaryMeeting> filteredMeetings = new ArrayList<>();

        for(DisciplinaryMeeting meeting : allMeetings){
            List<DisciplinaryMeetingParticipant> participantList = this.participantRepository.findAllByMeeting(meeting);
            List<DisciplinaryRecord> disciplinaryRecordList = this.recordRepository.findAllByMeeting(meeting);
            boolean prof = false;
            boolean record = false;
            boolean datee = false;
            for(DisciplinaryMeetingParticipant participant : participantList){
                if(participant.getProfessor() == professor1) prof = true;
            }
            for(DisciplinaryRecord record2 : disciplinaryRecordList){
                if(record2 == record1) record = true;
            }
            if(meeting.getDisciplinaryMeetingDate().equals(date1)){
                datee = true;
            }

            if(professor1 != null && date1 == null && record1 == null && prof)
                filteredMeetings.add(meeting);
            else if(professor1 == null && date1 != null && record1 == null && datee)
                filteredMeetings.add(meeting);
            else if(professor1 == null && date1 == null && record1 != null && record)
                filteredMeetings.add(meeting);
            else if(professor1 != null && date1 != null && record1 == null && prof && datee)
                filteredMeetings.add(meeting);
            else if(professor1 != null && date1 == null && record1 != null && prof && record)
                filteredMeetings.add(meeting);
            else if(professor1 == null && date1 != null && record1 != null && record && datee)
                filteredMeetings.add(meeting);
            else if(professor1 != null && date1 != null && record1 != null && record && datee && prof)
                filteredMeetings.add(meeting);
        }

        return new PageImpl<>(filteredMeetings);
    }

}

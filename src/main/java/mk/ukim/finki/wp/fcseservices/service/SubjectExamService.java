package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamDefinition;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.examschedule.SubjectExam;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface SubjectExamService {
    SubjectExam findByName(String name);

    Page<SubjectExam> findAll(Specification<SubjectExam> filter, Integer page, Integer size);

    List<SubjectExam> findAllByExamDefinitionAndExamSession(ExamDefinition examDefinition, ExamSession examSession);

    SubjectExam create(YearExamSession session, ExamDefinition definition);

    void initialize(String yes);

    SubjectExam update(String name, YearExamSession session,
                       Long durationMinutes, Long previousYearAttendantsNumber, Long previousYearTotalStudents,
                       Long attendantsNumber, Long totalStudents, Long expectedNumber,
                       Long numRepetitions, LocalDateTime fromTime, LocalDateTime toTime,
                       Set<String> roomNames, String comment);

    Set<Room> getAllRooms();

    Set<Room> getRoomsByNames(Set<String> roomNames);

    SubjectExam delete(String id);

    SubjectExam save(SubjectExam exam);

    boolean updateSubjectExamTime(String id, String newFromTime, String newToTime);

    void examCalculations(String yearExamSession);

    SubjectExam updateSubjectExamNumRepetitions(String id, Long numRepetitions);

    SubjectExam recalculateSubjectExam(String id);

    boolean checkInvalidDateTimeInput(String fromTime, String toTime);
}

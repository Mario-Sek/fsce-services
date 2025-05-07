package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamDefinition;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.examschedule.SubjectExam;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectExamRepository extends JpaRepository<SubjectExam, String> {
    @Override
    Optional<SubjectExam> findById(String s);

    Page<SubjectExam> findAll(Specification<SubjectExam> filter, Pageable page);

    List<SubjectExam> findAllBySession(YearExamSession session);

    @Modifying
    void deleteBySession(YearExamSession session);

    @Query("select se from SubjectExam se where se.definition = :examDefinition and se.session.session = :examSession")
    List<SubjectExam> findAllByDefinitionAndSessionSession(ExamDefinition examDefinition, ExamSession examSession);

    List<SubjectExam> findByDefinition_Subject(JoinedSubject subject);

    List<SubjectExam> findBySessionCycle(StudyCycle studyCycle);

    List<SubjectExam> findByRoomsContaining(Room room);
}

package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.dto.LateResultDto;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultsRepository extends JpaSpecificationRepository<Results, Long> {

    Optional<Results> findBySessionAndJoinedSubject(YearExamSession session, JoinedSubject joinedSubject);

    Optional<Results> findByJoinedSubjectAbbreviationAndSessionNameAndResultTypeAndUploadedBy(String abbreviation, String sessionName, String ResultType, Professor professor);

    @Query("SELECT p " +
            "FROM Professor p " +
            "LEFT JOIN Results r ON r.uploadedBy.id=p.id " +
            "WHERE r.id IS NULL")
    List<Professor> findProfessorsWithNoResults();


    @Query(value = "SELECT " +
            "p.id AS professorId, " +
            "p.name AS professorName, " +
            "js.name AS subjectName, " +
            "se.from_time AS examDate, " +
            "r.uploaded_at AS uploadDate, " +
            "yes.name AS sessionName " +
            "FROM professor p " +
            "JOIN results r ON r.uploaded_by_id = p.id " +
            "JOIN year_exam_session yes ON r.session_name = yes.name " +
            "JOIN joined_subject js ON js.abbreviation = r.joined_subject_abbreviation " +
            "JOIN subject_exam se ON se.session_name = yes.name " +
            "  AND se.definition_id IN (SELECT id FROM exam_definition WHERE subject_abbreviation = js.abbreviation) " +
            "JOIN exam_definition ex ON ex.id = se.definition_id " +
            "WHERE (" +
            "(ex.exam_session IN ('FIRST_MIDTERM', 'SECOND_MIDTERM') AND r.uploaded_at > (se.from_time + INTERVAL '14 DAY')) " +
            "OR " +
            "(ex.exam_session NOT IN ('FIRST_MIDTERM', 'SECOND_MIDTERM') AND r.uploaded_at > (se.from_time + INTERVAL '1 MONTH'))" +
            ")", nativeQuery = true)
    List<Object[]> findProfessorsWithLateResults();








}

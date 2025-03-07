package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultsRepository extends JpaSpecificationRepository<Results, Long> {

    Optional<Results> findBySessionAndJoinedSubject(YearExamSession session, JoinedSubject joinedSubject);

    Optional<Results> findByJoinedSubjectAbbreviationAndSessionNameAndResultTypeAndUploadedBy(String abbreviation, String sessionName, String ResultType, Professor professor);

}

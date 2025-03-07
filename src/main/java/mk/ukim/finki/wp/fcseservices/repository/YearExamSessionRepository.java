package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YearExamSessionRepository extends JpaRepository<YearExamSession, String> {

    Optional<YearExamSession> findByName(String name);

}

package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaryDecisionRepository extends JpaRepository<DisciplinaryDecision, Long> {
}

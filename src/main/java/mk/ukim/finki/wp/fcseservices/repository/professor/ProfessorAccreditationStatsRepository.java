package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorAccreditationStats;
import mk.ukim.finki.wp.fcseservices.repository.JpaSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ProfessorAccreditationStatsRepository extends JpaSpecificationRepository<ProfessorAccreditationStats, String> {
    Page<ProfessorAccreditationStats> findAll(Specification specification, Pageable pageable);
}

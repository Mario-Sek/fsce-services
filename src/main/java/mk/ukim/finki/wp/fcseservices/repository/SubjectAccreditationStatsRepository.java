package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.accreditations.SubjectAccreditationStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SubjectAccreditationStatsRepository extends JpaSpecificationRepository<SubjectAccreditationStats, String> {
    Page<SubjectAccreditationStats> findAll(Specification specification, Pageable pageable);
}

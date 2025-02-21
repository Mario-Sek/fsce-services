package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectNeedsView;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectNeedsRepository extends JpaSpecificationRepository<SubjectNeedsView, String> {
}
package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorEngagementRepository extends JpaSpecificationRepository<ProfessorEngagement, String> {

    Long countBySemester_Code(String semesterCode);
}

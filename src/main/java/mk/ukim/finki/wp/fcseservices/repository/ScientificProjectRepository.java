package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScientificProjectRepository extends JpaSpecificationRepository<ScientificProject, Long> {

}

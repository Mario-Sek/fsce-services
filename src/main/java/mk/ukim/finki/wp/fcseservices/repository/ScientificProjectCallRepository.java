package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScientificProjectCallRepository extends JpaRepository<ScientificProjectCall, Long> {

}

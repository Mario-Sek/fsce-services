package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScientificProjectProgrammeRepository extends JpaRepository<ScientificProjectProgramme, Long> {

}

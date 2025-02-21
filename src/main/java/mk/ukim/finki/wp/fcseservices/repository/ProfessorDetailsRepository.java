package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorDetailsRepository extends JpaRepository<ProfessorDetails, String> {

    List<ProfessorDetails> findAll();

    void deleteById(String id);

    ProfessorDetails findProfessorDetailsByProfessorId(String professorId);
}

package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorEducationRepository extends JpaRepository<ProfessorEducation, String> {

    List<ProfessorEducation> findAllByProfessor(Professor professor);

    ProfessorEducation findByEducationId(String id);

    void deleteAllByProfessor(Professor professor);
}

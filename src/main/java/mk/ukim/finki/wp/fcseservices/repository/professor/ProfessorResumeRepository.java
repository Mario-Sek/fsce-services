package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorResumeRepository extends JpaRepository<ProfessorResume, String> {
    ProfessorResume findByProfessorId(String id);
}

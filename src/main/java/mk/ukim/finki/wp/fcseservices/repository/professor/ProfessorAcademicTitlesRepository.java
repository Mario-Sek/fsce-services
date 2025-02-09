package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.professor.Professor;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorAcademicTitles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorAcademicTitlesRepository extends JpaRepository<ProfessorAcademicTitles, String> {

    ProfessorAcademicTitles findByProfessor(Professor professor);

    ProfessorAcademicTitles findByAcademicTitleId(String id);
}

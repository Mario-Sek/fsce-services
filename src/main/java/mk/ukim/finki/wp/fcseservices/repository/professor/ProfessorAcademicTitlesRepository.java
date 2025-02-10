package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorAcademicTitles;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorAcademicTitlesRepository extends JpaRepository<ProfessorAcademicTitles, String> {

    ProfessorAcademicTitles findByProfessor(Professor professor);

    ProfessorAcademicTitles findByAcademicTitleId(String id);
}

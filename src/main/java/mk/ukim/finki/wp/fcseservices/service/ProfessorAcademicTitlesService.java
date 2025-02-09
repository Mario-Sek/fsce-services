package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.professor.AcademicTitle;
import mk.ukim.finki.wp.fcseservices.model.professor.Professor;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorAcademicTitles;

public interface ProfessorAcademicTitlesService {

    ProfessorAcademicTitles findByProfessor(Professor professor);

    ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle);

    ProfessorAcademicTitles findByTitleId(String id);

    void deleteById(String id);

}
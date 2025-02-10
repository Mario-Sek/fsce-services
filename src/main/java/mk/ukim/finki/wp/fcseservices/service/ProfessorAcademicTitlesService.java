package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.AcademicTitle;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorAcademicTitles;

public interface ProfessorAcademicTitlesService {

    ProfessorAcademicTitles findByProfessor(Professor professor);

    ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle);

    ProfessorAcademicTitles findByTitleId(String id);

    void deleteById(String id);

}
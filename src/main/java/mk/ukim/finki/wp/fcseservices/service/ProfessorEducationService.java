package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.professor.Education;
import mk.ukim.finki.wp.fcseservices.model.professor.Professor;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorEducation;

import java.util.List;

public interface ProfessorEducationService {
    List<ProfessorEducation> listEducationByProfessor(Professor professor);

    ProfessorEducation save(Professor professor, Education education, Float order);

    void deleteById(String id);

    void deleteAllByProfessor(Professor professor);

    ProfessorEducation findByEducationId(String id);
}

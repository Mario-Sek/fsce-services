package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.Education;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorEducation;

import java.util.List;

public interface ProfessorEducationService {
    List<ProfessorEducation> listEducationByProfessor(Professor professor);

    ProfessorEducation save(Professor professor, Education education, Float order);

    void deleteById(String id);

    void deleteAllByProfessor(Professor professor);

    ProfessorEducation findByEducationId(String id);
}

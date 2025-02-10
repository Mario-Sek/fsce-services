package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.Education;
import mk.ukim.finki.wp.fcseservices.model.accreditations.EducationDegree;

import java.util.List;

public interface EducationService {

    Education save(String professorId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area);

    Education update(String educationId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area);

    void deleteById(String id);

    Education findById(String id);

    void deleteProfessorEducations(List<String> educationIds);

}

package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.professor.AcademicTitle;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorTitle;

public interface AcademicTitleService {
    AcademicTitle save(String id, String institution, ProfessorTitle title, String area, Short electionYear, Short decisionDocumentNumber);

    AcademicTitle findById(String id);

    void deleteById(String id);
}

package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramSubject;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramSubjectProfessor;

import java.util.List;

public interface StudyProgramSubjectProfessorService {


    List<Professor> professorsNotOnSubject(String subjectId);

    List<Professor> findProfessorsFromOtherProgramsForSubject(String subjectId, String currentProgramCode);
    void deleteProfessorForSubject(Professor professor, StudyProgramSubject studyProgramSubject);
    StudyProgramSubjectProfessor save(String newId,String studyProgramSubjectId, String professorId,Float newOrder);

    List<StudyProgramSubjectProfessor> professorsFromStudyProgramSubject(StudyProgramSubject studyProgramSubject);

    List<StudyProgramSubjectProfessor> saveAll(List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList);

    List<Professor> professorsOnAStudyProgram(String programCode);
}

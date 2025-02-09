package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.professor.Professor;
import mk.ukim.finki.wp.fcseservices.model.study_program.StudyProgramDetails;
import mk.ukim.finki.wp.fcseservices.model.subject.StudyProgramSubject;
import mk.ukim.finki.wp.fcseservices.model.subject.SubjectDetails;

import java.util.List;

public interface DisplayService {
    SubjectDetails getSubjectDetailsById(String subjectId);

    List<StudyProgramSubject> getSubjectPrograms(String subjectId);

    List<StudyProgramSubject> getProgramSubjects(String programCode);

    List<Professor> getSubjectProfessors(String subjectId);

    List<StudyProgramDetails> findAccreditationProgramsByCycle(String accreditation, StudyCycle cycle);

    StudyProgramDetails getStudyProgramDetailsById(String program);

}

package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramDetails;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramSubject;
import mk.ukim.finki.wp.fcseservices.model.accreditations.SubjectDetails;

import java.util.List;

public interface DisplayService {
    SubjectDetails getSubjectDetailsById(String subjectId);

    List<StudyProgramSubject> getSubjectPrograms(String subjectId);

    List<StudyProgramSubject> getProgramSubjects(String programCode);

    List<Professor> getSubjectProfessors(String subjectId);

    List<StudyProgramDetails> findAccreditationProgramsByCycle(String accreditation, StudyCycle cycle);

    StudyProgramDetails getStudyProgramDetailsById(String program);

}

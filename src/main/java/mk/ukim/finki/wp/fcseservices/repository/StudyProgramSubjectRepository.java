package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StudyProgramSubjectRepository extends JpaRepository<StudyProgramSubject, String> {

    List<StudyProgramSubject> findAllBySubjectId(String subjectId);

    List<StudyProgramSubject> findAllByStudyProgramCode(String studyProgramId);

    List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode);

}

package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramAccreditationDocument;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyProgramAccreditationDocumentRepository extends JpaRepository<StudyProgramAccreditationDocument, Long> {
    List<StudyProgramAccreditationDocument> findAllByStudyProgram(StudyProgramDetails studyProgramDetails);
}

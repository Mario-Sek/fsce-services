package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.AccreditationDocumentTypes;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyProgramAccreditationDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StudyProgramAccreditationDocumentService {
    List<StudyProgramAccreditationDocument> findAllByStudyProgramDetails(String studyProgramDetailsId);

    Optional<StudyProgramAccreditationDocument> findById(Long id);

    Optional<StudyProgramAccreditationDocument> save(String name, String fileExtension, AccreditationDocumentTypes type, String studyProgramDetailsId, MultipartFile document);

    void deleteById(Long id);
}

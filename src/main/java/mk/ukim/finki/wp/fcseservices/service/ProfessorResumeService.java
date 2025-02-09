package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorResume;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ProfessorResumeService {
    Optional<ProfessorResume> findById(String id);
    ProfessorResume save(String professorId, String resume, MultipartFile image);
    ProfessorResume update(ProfessorResume obj);

    ProfessorResume getByProfessorId(String id);
}

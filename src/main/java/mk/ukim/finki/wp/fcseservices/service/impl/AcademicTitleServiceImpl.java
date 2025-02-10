package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidId;
import mk.ukim.finki.wp.fcseservices.model.accreditations.AcademicTitle;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.repository.professor.AcademicTitleRepository;
import mk.ukim.finki.wp.fcseservices.service.AcademicTitleService;
import org.springframework.stereotype.Service;

@Service
public class AcademicTitleServiceImpl implements AcademicTitleService {

    private final AcademicTitleRepository academicTitleRepository;

    public AcademicTitleServiceImpl(AcademicTitleRepository academicTitleRepository) {
        this.academicTitleRepository = academicTitleRepository;
    }

    @Override
    public AcademicTitle findById(String id) {
        return academicTitleRepository.findById(id).orElseThrow(() -> new InvalidId(id));
    }

    @Override
    public AcademicTitle save(String id, String institution, ProfessorTitle title, String area, Short electionYear, Short decisionDocumentNumber) {
        AcademicTitle academicTitle = new AcademicTitle(id, institution, title, area, electionYear, decisionDocumentNumber);
        return academicTitleRepository.save(academicTitle);
    }

    @Override
    public void deleteById(String id) {
        academicTitleRepository.deleteById(id);
    }

}

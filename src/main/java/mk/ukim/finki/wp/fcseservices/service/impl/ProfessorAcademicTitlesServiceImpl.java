package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.accreditations.AcademicTitle;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorAcademicTitles;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorAcademicTitlesRepository;
import mk.ukim.finki.wp.fcseservices.service.ProfessorAcademicTitlesService;
import org.springframework.stereotype.Service;

@Service
public class ProfessorAcademicTitlesServiceImpl implements ProfessorAcademicTitlesService {

    private final ProfessorAcademicTitlesRepository professorAcademicTitlesRepository;


    public ProfessorAcademicTitlesServiceImpl(ProfessorAcademicTitlesRepository professorAcademicTitlesRepository) {
        this.professorAcademicTitlesRepository = professorAcademicTitlesRepository;
    }

    @Override
    public ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle) {
        ProfessorAcademicTitles professorAcademicTitles = new ProfessorAcademicTitles(id, professor, academicTitle);
        return professorAcademicTitlesRepository.save(professorAcademicTitles);
    }

    @Override
    public ProfessorAcademicTitles findByProfessor(Professor professor) {
        return professorAcademicTitlesRepository.findByProfessor(professor);
    }

    @Override
    public ProfessorAcademicTitles findByTitleId(String id) {
        return professorAcademicTitlesRepository.findByAcademicTitleId(id);
    }

    @Override
    public void deleteById(String id) {
        professorAcademicTitlesRepository.deleteById(id);
    }
}

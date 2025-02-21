package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.accreditations.Education;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorEducation;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorEducationRepository;
import mk.ukim.finki.wp.fcseservices.service.ProfessorEducationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorEducationServiceImpl implements ProfessorEducationService {

    private final ProfessorEducationRepository professorEducationRepository;

    public ProfessorEducationServiceImpl(ProfessorEducationRepository professorEducationRepository) {
        this.professorEducationRepository = professorEducationRepository;
    }

    @Override
    public List<ProfessorEducation> listEducationByProfessor(Professor professor) {
        return professorEducationRepository.findAllByProfessor(professor);
    }

    @Override
    public void deleteAllByProfessor(Professor professor) {
        professorEducationRepository.deleteAllByProfessor(professor);
    }

    @Override
    public void deleteById(String id) {
        professorEducationRepository.deleteById(id);
    }

    @Override
    public ProfessorEducation findByEducationId(String id) {
        return professorEducationRepository.findByEducationId(id);
    }


    @Override
    public ProfessorEducation save(Professor professor, Education education, Float order) {
        ProfessorEducation professorEducation = new ProfessorEducation(education.getId(), professor, education, order);

        return professorEducationRepository.save(professorEducation);
    }
}

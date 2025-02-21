package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectStatus;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorRepository;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectCallRepository;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectProgrammeRepository;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectRepository;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScientificProjectServiceImpl implements ScientificProjectService {

    private final ScientificProjectRepository scientificProjectRepository;

    private final ProfessorRepository professorRepository;

    private final ScientificProjectCallRepository scientificProjectCallRepository;

    private final ScientificProjectProgrammeRepository scientificProjectProgrammeRepository;


    public ScientificProjectServiceImpl(ScientificProjectRepository scientificProjectRepository, ProfessorRepository professorRepository,
                                        ScientificProjectCallRepository scientificProjectCallRepository, ScientificProjectProgrammeRepository
                                                scientificProjectProgrammeRepository) {
        this.scientificProjectRepository = scientificProjectRepository;
        this.professorRepository = professorRepository;
        this.scientificProjectCallRepository = scientificProjectCallRepository;
        this.scientificProjectProgrammeRepository = scientificProjectProgrammeRepository;
    }

    @Override
    public List<ScientificProject> findAll() {
        return this.scientificProjectRepository.findAll();
    }

    @Override
    public Optional<ScientificProject> findById(Long id) {
        return this.scientificProjectRepository.findById(id);
    }

    @Override
    public Optional<ScientificProject> save(ScientificProjectStatus status, String name, String keywords, String goalsDescription, String relatedPublicationsOrProjects,
                                            String report, String expectedResults, String professorId, Long projectCallId,
                                            Long programmeId) {
        Professor professor = this.professorRepository.findById(professorId).orElseThrow();
        ScientificProjectCall scientificProjectCall = this.scientificProjectCallRepository.findById(projectCallId)
                .orElseThrow(); //TODO: exception
        ScientificProjectProgramme scientificProjectProgramme = this.scientificProjectProgrammeRepository
                .findById(programmeId).orElseThrow(); //TODO: exception
        ScientificProject scientificProject = new ScientificProject(null,
                status, name, keywords, goalsDescription, relatedPublicationsOrProjects, report,
                expectedResults, professor, scientificProjectCall, scientificProjectProgramme);
        return Optional.of(this.scientificProjectRepository.save(scientificProject));
    }

    @Override
    public Optional<ScientificProject> edit(Long id, ScientificProjectStatus status, String name, String keywords, String goalsDescription,
                                            String relatedPublicationsOrProjects, String report, String expectedResults, String professorId,
                                            Long projectCallId, Long programmeId) {
        ScientificProject scientificProject = this.findById(id).orElseThrow(); //TODO: exception
        scientificProject.setStatus(status);
        scientificProject.setName(name);
        scientificProject.setKeywords(keywords);
        scientificProject.setGoalsDescription(goalsDescription);
        scientificProject.setRelatedPublicationsOrProjects(relatedPublicationsOrProjects);
        scientificProject.setReport(report);
        scientificProject.setExpectedResults(expectedResults);
        Professor professor = this.professorRepository.findById(professorId).orElseThrow();
        ScientificProjectCall scientificProjectCall = this.scientificProjectCallRepository
                .findById(projectCallId).orElseThrow(); //TODO: exception
        ScientificProjectProgramme scientificProjectProgramme = this.scientificProjectProgrammeRepository
                .findById(programmeId).orElseThrow(); //TODO: exception
        scientificProject.setCoordinator(professor);
        scientificProject.setProjectCall(scientificProjectCall);
        scientificProject.setProgramme(scientificProjectProgramme);
        return Optional.of(this.scientificProjectRepository.save(scientificProject));
    }

    @Override
    public void deleteById(Long id) {
        this.scientificProjectRepository.deleteById(id);
    }

    @Override
    public Page<ScientificProject> filterAndPaginateJoinedScientificProjects(String programmeName,
                                                                             String grantHolderName,
                                                                             Boolean international,
                                                                             ScientificProjectStatus status,
                                                                             String name,
                                                                             String professorName,
                                                                             String scientificProjectCallName,
                                                                             int pageNum,
                                                                             int pageSize) {
        return null;
    }


}

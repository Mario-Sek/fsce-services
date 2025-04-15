package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectProgrammeRepository;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScientificProjectProgrammeServiceImpl implements ScientificProjectProgrammeService {

    private final ScientificProjectProgrammeRepository scientificProjectProgrammeRepository;

    public ScientificProjectProgrammeServiceImpl(ScientificProjectProgrammeRepository scientificProjectProgrammeRepository) {
        this.scientificProjectProgrammeRepository = scientificProjectProgrammeRepository;
    }

    @Override
    public List<ScientificProjectProgramme> findAll() {
        return this.scientificProjectProgrammeRepository.findAll();
    }
}

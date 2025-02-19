package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinarySanction;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinarySanctionRepository;
import mk.ukim.finki.wp.fcseservices.service.DisciplinarySanctionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinarySanctionServiceImpl implements DisciplinarySanctionService {

    private final DisciplinarySanctionRepository disciplinarySanctionRepository;

    public DisciplinarySanctionServiceImpl(DisciplinarySanctionRepository disciplinarySanctionRepository) {
        this.disciplinarySanctionRepository = disciplinarySanctionRepository;
    }

    @Override
    public List<DisciplinarySanction> findAllSanctions() {
        return disciplinarySanctionRepository.findAll();
    }
}

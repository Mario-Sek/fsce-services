package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.projects.GrantHolder;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectProgrammeRepository;
import mk.ukim.finki.wp.fcseservices.service.GrantHolderService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectProgrammeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScientificProjectProgrammeServiceImpl implements ScientificProjectProgrammeService {

    private final ScientificProjectProgrammeRepository scientificProjectProgrammeRepository;
    private final GrantHolderService grantHolderService;


    public ScientificProjectProgrammeServiceImpl(ScientificProjectProgrammeRepository scientificProjectProgrammeRepository, GrantHolderService grantHolderService) {
        this.scientificProjectProgrammeRepository = scientificProjectProgrammeRepository;
        this.grantHolderService = grantHolderService;
    }


    @Override
    public List<ScientificProjectProgramme> findAll() {
        return scientificProjectProgrammeRepository.findAll();
    }

    @Override
    public Optional<ScientificProjectProgramme> findById(Long id) {
        return scientificProjectProgrammeRepository.findById(id);
    }

    @Override
    public Optional<ScientificProjectProgramme> save(String name, Long grantHolderId, Boolean international) {
        GrantHolder holder = grantHolderService.findById(grantHolderId).orElseThrow();  //todo exception
        ScientificProjectProgramme programme = new ScientificProjectProgramme(null, name, holder,international);
        return Optional.of(scientificProjectProgrammeRepository.save(programme));
    }

    @Override
    public Optional<ScientificProjectProgramme> edit(Long id, String name, Long grantHolderId, Boolean international) {
        ScientificProjectProgramme programme = scientificProjectProgrammeRepository.findById(id).orElseThrow(); //todo exception
        programme.setName(name);
        GrantHolder holder = grantHolderService.findById(grantHolderId).orElseThrow(); //todo exception
        programme.setGrantHolder(holder);
        programme.setInternational(international);

        return Optional.of(scientificProjectProgrammeRepository.save(programme));
    }

    @Override
    public void deleteById(Long id) {
        scientificProjectProgrammeRepository.deleteById(id);
    }

    @Override
    public Page<ScientificProjectProgramme> findAllWithPagination(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return scientificProjectProgrammeRepository.findAll(pageRequest);
    }
}

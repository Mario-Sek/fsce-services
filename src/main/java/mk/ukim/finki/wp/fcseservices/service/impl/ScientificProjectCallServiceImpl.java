package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.projects.GrantHolder;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;
import mk.ukim.finki.wp.fcseservices.model.exceptions.GrantHolderNotFound;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ScientificProjectCallNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectCallRepository;
import mk.ukim.finki.wp.fcseservices.service.GrantHolderService;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectCallService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ScientificProjectCallServiceImpl implements ScientificProjectCallService {


    private final ScientificProjectCallRepository scientificProjectCallRepository;
    private final GrantHolderService grantHolderService;

    public ScientificProjectCallServiceImpl(ScientificProjectCallRepository scientificProjectCallRepository, GrantHolderService grantHolderService) {
        this.scientificProjectCallRepository = scientificProjectCallRepository;
        this.grantHolderService = grantHolderService;
    }


    @Override
    public List<ScientificProjectCall> findAll() {
        return this.scientificProjectCallRepository.findAll();
    }

    @Override
    public Optional<ScientificProjectCall> findById(Long id) {
        return this.scientificProjectCallRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        ScientificProjectCall scientificProjectCall = this.findById(id).orElseThrow(ScientificProjectCallNotFoundException::new);
        this.scientificProjectCallRepository.delete(scientificProjectCall);
    }

    @Override
    public Page<ScientificProjectCall> findAllByPagination(Pageable pageable) {
        return this.scientificProjectCallRepository.findAll(pageable);
    }
}

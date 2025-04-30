package mk.ukim.finki.wp.fcseservices.service.impl;


import mk.ukim.finki.wp.fcseservices.model.exceptions.*;
import mk.ukim.finki.wp.fcseservices.model.projects.*;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectCallRepository;
import mk.ukim.finki.wp.fcseservices.repository.ScientificProjectProgrammeRepository;
import mk.ukim.finki.wp.fcseservices.service.ScientificProjectCallService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;


@Service
public class ScientificProjectCallServiceImpl implements ScientificProjectCallService {


    private final ScientificProjectCallRepository scientificProjectCallRepository;
    private final ScientificProjectProgrammeRepository scientificProjectProgrammeRepository;

    public ScientificProjectCallServiceImpl(ScientificProjectCallRepository scientificProjectCallRepository, ScientificProjectProgrammeRepository scientificProjectProgrammeRepository) {
        this.scientificProjectCallRepository = scientificProjectCallRepository;
        this.scientificProjectProgrammeRepository = scientificProjectProgrammeRepository;
    }

    @Override
    public Optional<ScientificProjectCall> save(String name, LocalDateTime createdAt, LocalDateTime applicationDeadLine, Long programme, ScientificCallStatus status) {
        ScientificProjectProgramme projectProgramme = scientificProjectProgrammeRepository.findById(programme).orElseThrow(ScientificProjectProgrammeNotFoundException::new);

        return Optional.of(this.scientificProjectCallRepository.save(new ScientificProjectCall(name, createdAt, applicationDeadLine, projectProgramme, status)));
    }

    @Override
    public Optional<ScientificProjectCall> update(Long id, String name, LocalDateTime createdAt, LocalDateTime applicationDeadLine, Long programme, ScientificCallStatus status) {
        ScientificProjectCall existingCall = this.findById(id)
                .orElseThrow(ScientificProjectCallNotFoundException::new);

        existingCall.setName(name);
        existingCall.setCreatedAt(createdAt);
        existingCall.setApplicationDeadline(applicationDeadLine);
        var projectProgramme = scientificProjectProgrammeRepository.findById(programme).orElseThrow(ScientificProjectProgrammeNotFoundException::new);
        existingCall.setProgramme(projectProgramme);
        existingCall.setStatus(status);

        return Optional.of(this.scientificProjectCallRepository.save(existingCall));
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
    public Page<ScientificProjectCall> findAllByPagination(Long programmeId,
                                                           Long grantHolderId,
                                                           Boolean programmeInternational,
                                                           ScientificCallStatus status,
                                                           String name,
                                                           Integer pageNum, Integer pageSize) {
        Specification<ScientificProjectCall> specification = Specification
                .where(filterContainsText(ScientificProjectCall.class, "name", name))
                .and(filterEqualsV(ScientificProjectCall.class, "status", status))
                .and(filterEquals(ScientificProjectCall.class, "programme.id", programmeId))
                .and(filterEquals(ScientificProjectCall.class, "programme.grantHolder.id", grantHolderId))
                .and(filterEqualsV(ScientificProjectCall.class, "programme.international", programmeInternational));

        return this.scientificProjectCallRepository.findAll(
                specification,
                PageRequest.of(pageNum - 1, pageSize)
        );
    }
}

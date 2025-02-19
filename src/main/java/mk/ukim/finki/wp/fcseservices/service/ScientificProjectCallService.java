package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ScientificProjectCallService {
    List<ScientificProjectCall> findAll();

    Optional<ScientificProjectCall> findById(Long id);

    void delete(Long id);

    Page<ScientificProjectCall> findAllByPagination(Pageable pageable);
}

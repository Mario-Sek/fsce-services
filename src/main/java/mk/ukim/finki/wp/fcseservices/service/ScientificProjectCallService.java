package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificCallStatus;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectCall;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScientificProjectCallService {
    Optional<ScientificProjectCall> update(Long id, String name, LocalDateTime createdAt, LocalDateTime applicationDeadLine, Long programme, ScientificCallStatus status);

    Optional<ScientificProjectCall> save(String name, LocalDateTime createdAt, LocalDateTime applicationDeadLine, Long programme, ScientificCallStatus status);

    List<ScientificProjectCall> findAll();

    Optional<ScientificProjectCall> findById(Long id);

    void delete(Long id);

    Page<ScientificProjectCall> findAllByPagination(Long programmeId, Long grantHolderId, Boolean programmeInternational, ScientificCallStatus status, String name, Integer pageNum, Integer pageSize);
}

package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface ScientificProjectProgrammeService {

    List<ScientificProjectProgramme> findAll();

    Optional<ScientificProjectProgramme> findById(Long id);


    Optional<ScientificProjectProgramme> save(String name, Long grantHolderId, Boolean international);

    Optional<ScientificProjectProgramme> edit(Long id, String name, Long grantHolderId, Boolean international);

    void deleteById(Long id);

    Page<ScientificProjectProgramme> findAllWithPagination(int pageNum, int pageSize);

}

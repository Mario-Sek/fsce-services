package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ScientificProjectService {

    List<ScientificProject> findAll();

    Optional<ScientificProject> findById(Long id);


    Optional<ScientificProject> save(ScientificProjectStatus status, String name, String keywords, String goalsDescription, String relatedPublicationsOrProjects,
                                     String report, String expectedResults, String professorId, Long projectCallId,
                                     Long programmeId);

    Optional<ScientificProject> edit(Long id, ScientificProjectStatus status, String name, String keywords, String goalsDescription,
                                     String relatedPublicationsOrProjects, String report, String expectedResults, String professorId,
                                     Long projectCallId, Long programmeId);

    void deleteById(Long id);

    Page<ScientificProject> filterAndPaginateJoinedScientificProjects(String programmeName, String grantHolderName, Boolean international,
                                                            ScientificProjectStatus status, String name, String professorName,
                                                            String scientificProjectCallName, int pageNum, int pageSize);
}

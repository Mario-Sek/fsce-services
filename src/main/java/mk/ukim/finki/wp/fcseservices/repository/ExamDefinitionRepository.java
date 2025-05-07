package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamDefinitionRepository extends JpaRepository<ExamDefinition, String>, JpaSpecificationRepository<ExamDefinition, String> {

    List<ExamDefinition> findAll();

    @Override
    Page<ExamDefinition> findAll(Specification<ExamDefinition> filter, Pageable pageable);
}

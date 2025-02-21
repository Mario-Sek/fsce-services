package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectNeedsView;
import mk.ukim.finki.wp.fcseservices.repository.SubjectNeedsRepository;
import mk.ukim.finki.wp.fcseservices.service.SubjectNeedsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterContainsText;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;

@AllArgsConstructor
@Service
public class SubjectNeedsServiceImpl implements SubjectNeedsService {

    private final SubjectNeedsRepository repository;

    @Override
    public Page<SubjectNeedsView> list(String semesterCode, String name, int page, int results, Boolean lessThanPRG, Boolean lessThanPR, Boolean lessThanARG, Boolean lessThanAR, Boolean lessThanCL) {
        PageRequest pageRequest = PageRequest.of(page - 1, results,
                Sort.by(Sort.Direction.ASC, "subjectName"));
        Specification<SubjectNeedsView> spec = applyFilters(semesterCode, name, lessThanPRG, lessThanPR, lessThanARG, lessThanAR, lessThanCL);

        return repository.findAll(spec, pageRequest);
    }

    @Override
    public List<SubjectNeedsView> findAllForExport(String semesterCode, String name, Boolean lessThanPRG, Boolean lessThanPR, Boolean lessThanARG, Boolean lessThanAR, Boolean lessThanCL) {
        Specification<SubjectNeedsView> spec = applyFilters(semesterCode, name, lessThanPRG, lessThanPR, lessThanARG, lessThanAR, lessThanCL);
        return repository.findAll(spec);
    }

    private Specification<SubjectNeedsView> applyFilters(String semesterCode, String name, Boolean lessThanPRG, Boolean lessThanPR, Boolean lessThanARG, Boolean lessThanAR, Boolean lessThanCL) {
        Specification<SubjectNeedsView> spec = Specification
                .where(filterEquals(SubjectNeedsView.class, "semesterCode", semesterCode))
                .and(filterContainsText(SubjectNeedsView.class, "subjectName", name));

        if (lessThanPRG) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("nAssignedProfessors"), root.get("nGroups")));
        }

        if (lessThanPR) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("nReqProfessors"), root.get("nGroups")));
        }

        if (lessThanARG) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("nAssignedAssistants"), root.get("nGroups")));
        }

        if (lessThanAR) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("nReqAssistants"), root.get("nGroups")));
        }

        if (lessThanCL) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("nCoveredLabGroups"), root.get("nGroups")));
        }

        return spec;
    }

}

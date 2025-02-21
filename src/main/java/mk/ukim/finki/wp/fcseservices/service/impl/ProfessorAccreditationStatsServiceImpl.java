package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorAccreditationStats;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorAccreditationStatsRepository;
import mk.ukim.finki.wp.fcseservices.service.AccreditationService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorAccreditationStatsService;
import mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ProfessorAccreditationStatsServiceImpl implements ProfessorAccreditationStatsService {

    private final ProfessorAccreditationStatsRepository professorAccreditationStatsRepository;
    private final AccreditationService accreditationService;

    public ProfessorAccreditationStatsServiceImpl(ProfessorAccreditationStatsRepository professorAccreditationStatsRepository, AccreditationService accreditationService) {
        this.professorAccreditationStatsRepository = professorAccreditationStatsRepository;
        this.accreditationService = accreditationService;
    }

    @Override
    public Page<ProfessorAccreditationStats> findAllWithPaginationAndFilters(Integer pageNum,
                                                                             Integer results,
                                                                             String accreditationYear,
                                                                             StudyCycle studyCycle,
                                                                             String nameSearch,
                                                                             String emailSearch,
                                                                             ProfessorTitle titleFilter,
                                                                             SemesterType semesterSearch) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results, Sort.by(Sort.Direction.DESC, "numSubjectParts"));

        if (accreditationYear == null || accreditationYear.isEmpty()) {
            accreditationYear = accreditationService.findActiveAccreditation().getYear();
        }

        Specification<ProfessorAccreditationStats> spec = Specification.where(
                FieldFilterSpecification.filterEquals(ProfessorAccreditationStats.class, "accreditationYear", accreditationYear)
                        .and(FieldFilterSpecification.filterEquals(ProfessorAccreditationStats.class, "cycle", studyCycle))
                        .and(FieldFilterSpecification.filterContainsText(ProfessorAccreditationStats.class, "professor.name", nameSearch))
                        .and(FieldFilterSpecification.filterContainsText(ProfessorAccreditationStats.class, "professor.email", emailSearch))
                        .and(FieldFilterSpecification.filterEqualsV(ProfessorAccreditationStats.class, "professor.title", titleFilter))
                        .and(FieldFilterSpecification.filterEqualsV(ProfessorAccreditationStats.class, "semester", semesterSearch))
        );

        return professorAccreditationStatsRepository.findAll(spec, pageRequest);
    }
}

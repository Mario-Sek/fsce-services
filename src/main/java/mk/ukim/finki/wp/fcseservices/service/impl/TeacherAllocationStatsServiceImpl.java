package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;

import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherAllocationStats;
import mk.ukim.finki.wp.fcseservices.repository.TeacherAllocationStatsRepository;
import mk.ukim.finki.wp.fcseservices.service.TeacherAllocationStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;
import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEqualsV;

@AllArgsConstructor
@Service
public class TeacherAllocationStatsServiceImpl implements TeacherAllocationStatsService {

    private final TeacherAllocationStatsRepository repository;

    @Override
    public Page<TeacherAllocationStats> list(String semesterCode,
                                             String subjectAbbreviation,
                                             String professorId,
                                             ProfessorTitle professorTitle,
                                             int page, int results) {
        Specification<TeacherAllocationStats> spec = Specification
                .where(filterEquals(TeacherAllocationStats.class, "semester.code", semesterCode))
                .and(filterEquals(TeacherAllocationStats.class, "subject.abbreviation", subjectAbbreviation))
                .and(filterEquals(TeacherAllocationStats.class, "professor.id", professorId))
                .and(filterEqualsV(TeacherAllocationStats.class, "professor.title", professorTitle));
        return repository.findAll(spec, PageRequest.of(page - 1, results));
    }

    @Override
    public List<TeacherAllocationStats> findAll() {
        return repository.findAll();
    }


}

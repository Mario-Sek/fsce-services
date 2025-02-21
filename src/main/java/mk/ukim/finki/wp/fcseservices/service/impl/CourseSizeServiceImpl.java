package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.CourseSize;
import mk.ukim.finki.wp.fcseservices.repository.CourseSizeRepository;
import mk.ukim.finki.wp.fcseservices.service.CourseSizeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;

@AllArgsConstructor
@Service
public class CourseSizeServiceImpl implements CourseSizeService {

    private final CourseSizeRepository repository;


    @Override
    public Page<CourseSize> list(String semesterCode, String subject, Long groupId, Integer pageNum, Integer results) {
        Specification<CourseSize> spec = Specification
                .where(filterEquals(CourseSize.class, "semesterCode", semesterCode))
                .and(filterEquals(CourseSize.class, "joinedSubjectAbbreviation", subject));
        return repository.findAll(spec, PageRequest.of(pageNum - 1, results));
    }

    @Override
    public List<CourseSize> getCourseSizesBySubject(String abbreviation) {
        return repository.findCourseSizesByJoinedSubjectAbbreviation(abbreviation);
    }

    @Override
    public List<CourseSize> getCourseSizesBySubjectAndSemester(String abbreviation, String semesterCode) {
        return repository.findAllBySemesterCodeAndJoinedSubjectAbbreviation(semesterCode, abbreviation);
    }
}

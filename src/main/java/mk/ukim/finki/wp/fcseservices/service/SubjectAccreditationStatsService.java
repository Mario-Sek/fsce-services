package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.subject.SubjectAccreditationStats;
import org.springframework.data.domain.Page;

public interface SubjectAccreditationStatsService {
    Page<SubjectAccreditationStats> findAllWithPaginationAndFilters(Integer pageNumber, Integer result, String subjectCode, String professorCode, String studyProgramCode, String selectedAccreditationYear);
}

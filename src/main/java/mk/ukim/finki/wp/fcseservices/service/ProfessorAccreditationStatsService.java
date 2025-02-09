package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorAccreditationStats;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.semester.SemesterType;
import org.springframework.data.domain.Page;

public interface ProfessorAccreditationStatsService {
    Page<ProfessorAccreditationStats> findAllWithPaginationAndFilters(Integer pageNum, Integer results, String accreditationYear,
                                                                      StudyCycle studyCycle, String nameSearch, String emailSearch, ProfessorTitle titleFilter, SemesterType semesterType);
}

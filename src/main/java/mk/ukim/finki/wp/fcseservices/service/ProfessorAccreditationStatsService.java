package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.accreditations.ProfessorAccreditationStats;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import org.springframework.data.domain.Page;

public interface ProfessorAccreditationStatsService {
    Page<ProfessorAccreditationStats> findAllWithPaginationAndFilters(Integer pageNum, Integer results, String accreditationYear,
                                                                      StudyCycle studyCycle, String nameSearch, String emailSearch, ProfessorTitle titleFilter, SemesterType semesterType);
}

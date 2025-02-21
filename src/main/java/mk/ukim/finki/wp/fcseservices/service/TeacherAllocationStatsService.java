package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherAllocationStats;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherAllocationStatsService {

    Page<TeacherAllocationStats> list(String semesterCode,
                                      String subjectAbbreviation,
                                      String professorId,
                                      ProfessorTitle professorTitle,
                                      int page,
                                      int results);

    List<TeacherAllocationStats> findAll();
}

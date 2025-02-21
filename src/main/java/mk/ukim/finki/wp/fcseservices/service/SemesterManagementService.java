package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterState;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SemesterManagementService {
    Semester saveSemester(String year, SemesterType semesterType,
                          LocalDate startDate,
                          LocalDate endDate, LocalDate enrollmentStartDate,
                          LocalDate enrollmentEndDate,
                          List<StudyCycle> cycles,
                          SemesterState state);
    Optional<Semester> updateSemester(String code, String year, SemesterType semesterType,
                        LocalDate startDate,
                        LocalDate endDate, LocalDate enrollmentStartDate,
                        LocalDate enrollmentEndDate,
                        List<StudyCycle> cycles,
                        SemesterState state);

    List<Semester> getAllSemesters();

    Optional<Semester> getSemesterById(String code);

    Semester getLastSemester();

    Page<Semester> list(int page, int results);

    Semester getActiveSemester();

}

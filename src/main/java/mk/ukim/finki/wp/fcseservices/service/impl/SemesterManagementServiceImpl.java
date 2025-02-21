package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.repository.SemesterRepository;
import mk.ukim.finki.wp.fcseservices.service.SemesterManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.exceptions.NoActiveSemesterException;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SemesterManagementServiceImpl implements SemesterManagementService {
    private final SemesterRepository semesterManagementRepository;


    @Override
    public Semester saveSemester(String year, SemesterType semesterType,
                                 LocalDate startDate,
                                 LocalDate endDate, LocalDate enrollmentStartDate,
                                 LocalDate enrollmentEndDate,
                                 List<StudyCycle> cycles,
                                 SemesterState state) {
        //mora da go imame i code zatoa shto ne e generated
        String code = year + "-W";
        if (semesterType == SemesterType.SUMMER)
            code = year + "-S";
        return semesterManagementRepository.save(new Semester(code, year, semesterType, startDate, endDate, enrollmentStartDate, enrollmentEndDate, cycles, state));
    }

    @Override
    public Optional<Semester> updateSemester(String code, String year, SemesterType semesterType,
                                             LocalDate startDate, LocalDate endDate, LocalDate enrollmentStartDate,
                                             LocalDate enrollmentEndDate, List<StudyCycle> cycles,
                                             SemesterState state) {
        //najverojatno treba tuka da ima custom exception
        Semester semester = semesterManagementRepository.findById(code).orElseThrow(() -> new IllegalArgumentException(code));
        semester.setYear(year);
        semester.setSemesterType(semesterType);
        semester.setStartDate(startDate);
        semester.setEndDate(endDate);
        semester.setEnrollmentStartDate(enrollmentStartDate);
        semester.setEnrollmentEndDate(enrollmentEndDate);
        semester.setCycle(cycles);
        semester.setState(state);
        return Optional.of(semesterManagementRepository.save(semester));
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterManagementRepository.findAll(Sort.by(
                Sort.Order.desc("startDate"),
                Sort.Order.desc("year")
        ));
    }

    @Override
    public Optional<Semester> getSemesterById(String code) {
        //ako frlam exception tuka, funkcijata za add/edit ne raboti zatoa shto prvichnoto prebaruvanje po id
        //vo sluchaj na add frla error
        return semesterManagementRepository.findById(code);
    }

    @Override
    public Semester getLastSemester() {
        return this.semesterManagementRepository.findFirstByOrderByCodeDesc();
    }

    @Override
    public Page<Semester> list(int page, int results) {
        return semesterManagementRepository.findAll(PageRequest.of(page - 1, results,
                Sort.by(
                        Sort.Order.desc("startDate"),
                        Sort.Order.desc("year")

                )));
    }


    @Override
    public Semester getActiveSemester() {
        return semesterManagementRepository.findFirstByStateIn(List.of(
                SemesterState.STARTED,
                SemesterState.SCHEDULE_PREPARATION,
                SemesterState.TEACHER_SUBJECT_ALLOCATION,
                SemesterState.STUDENTS_ENROLLMENT,
                SemesterState.DATA_COLLECTION
        )).orElseThrow(NoActiveSemesterException::new);
    }

}

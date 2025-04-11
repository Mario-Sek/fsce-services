package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.*;
import mk.ukim.finki.wp.fcseservices.model.exceptions.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.repository.ProfessorRepository;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryRecordService;
import mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.*;


@Service
@RequiredArgsConstructor
public class DisciplinaryRecordServiceImpl implements DisciplinaryRecordService {

    private final DisciplinaryRecordRepository reportRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final JoinedSubjectRepository joinedSubjectRepository;
    private final DisciplinaryTypeRepository categoryRepository;
    private final DisciplinaryMeetingRepository meetingRepository;
    private final DisciplinarySanctionRepository sanctionRepository;

    @Override
    public DisciplinaryRecord createNewReport(Integer degree, String note, String professorUsername, String studentIndex, String joinedSubjectId, String categoryId,
                                              DisciplinaryStatus status, LocalDate date)
            throws StudentNotFoundException,
            ProfessorNotFoundException,
            JoinedSubjectNotFoundException,
            DisciplinaryTypeNotFoundException {

        Result result = getResult(professorUsername,studentIndex, joinedSubjectId, categoryId);

        DisciplinaryRecord report = new DisciplinaryRecord();
        report.setStudent(result.student);
        report.setType(result.category);
        report.setReportingDate(date);
        report.setDescription(note);
        report.setReporter(result.professor);
        report.setJoinedSubject(result.joinedSubject);
        report.setSeverity(Float.valueOf(degree));
        report.setStatus(status);
        report.setNotifiedStudent(false);
        report.setStudentLastAccess(null);
        report.setAdmittedByStudent(false);
        report.setStudentNote("");
        report.setStudentLastNote(null);

        report.setSuggestedDisciplinarySanction(null);
        report.setMeeting(null);

        return this.reportRepository.save(report);
    }

    @Override
    public Page<DisciplinaryRecord> findAllReports(DisciplinaryStatus status, String professorId, String subjectId, Long meetingId, String suggestedSanction, int pageNumber, int pageSize) {
        Specification<DisciplinaryRecord> specification = Specification
                .where(filterEquals(DisciplinaryRecord.class, "suggestedDisciplinarySanction.id", suggestedSanction))
                .and(filterEquals(DisciplinaryRecord.class, "reporter.id", professorId))
                .and(filterContainsText(DisciplinaryRecord.class, "joinedSubject.name", subjectId))
                .and(filterEquals(DisciplinaryRecord.class, "meeting.id", meetingId))
                .and(filterEqualsV(DisciplinaryRecord.class, "status", status));


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "reportingDate"));

        return reportRepository.findAll(specification,pageable);
    }





    @Override
    public List<DisciplinaryRecord> findAllReportsForStudent(String index) throws StudentNotFoundException {
        Student student = this.studentRepository.findById(index)
                .orElseThrow(() -> new StudentNotFoundException("Student is not found"));
        return this.reportRepository.findAllByStudent(student);
    }

    @Override
    public DisciplinaryRecord findReportById(String id) throws DisciplinaryRecordNotFoundException {
        return this.reportRepository.findById(id)
                .orElseThrow(() -> new DisciplinaryRecordNotFoundException("Report is not found"));
    }

    @Override
    public DisciplinaryRecord updateReport(String reportId, Float degree, String note, String professorUsername, String studentIndex, String joinedSubjectId,
                                           String categoryId, DisciplinaryStatus status, LocalDate date)
            throws DisciplinaryRecordNotFoundException,
            StudentNotFoundException,
            ProfessorNotFoundException,
            JoinedSubjectNotFoundException,
            DisciplinaryTypeNotFoundException {

        DisciplinaryRecord report = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new DisciplinaryRecordNotFoundException("Report is not found"));

        Result result = getResult(professorUsername, studentIndex, joinedSubjectId, categoryId);

        report.setStudent(result.student);
        report.setType(result.category);
        report.setReportingDate(date);
        report.setDescription(note);
        report.setReporter(result.professor);
        report.setJoinedSubject(result.joinedSubject);
        report.setSeverity(Float.valueOf(degree));
        report.setStatus(status);

        return this.reportRepository.save(report);
    }

    @Override
    public DisciplinaryRecord updateReportForStudent(String reportId, Boolean admitReport, String studentNote) throws DisciplinaryRecordNotFoundException {
        DisciplinaryRecord report = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new DisciplinaryRecordNotFoundException("Report is not found"));

        report.setNotifiedStudent(true);
        report.setStudentLastAccess(LocalDateTime.now());
        report.setAdmittedByStudent(admitReport);
        report.setStudentNote(studentNote);
        report.setStudentLastNote(LocalDateTime.now());

        return this.reportRepository.save(report);
    }

    @Override
    public DisciplinaryRecord setSanctionForRecord(String id, String decisionName, String decisionDescription) throws DisciplinaryRecordNotFoundException {
        DisciplinaryRecord disciplinaryRecord = reportRepository.findById(id).orElseThrow(() -> new DisciplinaryRecordNotFoundException("Record not found with provided id"));
        if (disciplinaryRecord.getSuggestedDisciplinarySanction() != null) {
            disciplinaryRecord.getSuggestedDisciplinarySanction().setName(decisionName);
            disciplinaryRecord.getSuggestedDisciplinarySanction().setDescription(decisionDescription);
            disciplinaryRecord.setStatus(DisciplinaryStatus.PROCESSED);
        } else {
            DisciplinarySanction sanction = new DisciplinarySanction();
            sanction.setName(decisionName);
            sanction.setDescription(decisionDescription);

            sanctionRepository.save(sanction);
            disciplinaryRecord.setSuggestedDisciplinarySanction(sanction);
            disciplinaryRecord.setStatus(DisciplinaryStatus.PROCESSED);
        }

        return reportRepository.save(disciplinaryRecord);
    }

    @Override
    public DisciplinaryRecord updateStudentNotification(String reportId) throws DisciplinaryRecordNotFoundException {
        DisciplinaryRecord report = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new DisciplinaryRecordNotFoundException("Record not found with provided id"));

        report.setNotifiedStudent(true);
        report.setStudentLastAccess(LocalDateTime.now());

        return this.reportRepository.save(report);
    }

    private Result getResult(String professorUsername, String studentIndex, String joinedSubjectId, String categoryId) throws ProfessorNotFoundException, JoinedSubjectNotFoundException, StudentNotFoundException, DisciplinaryTypeNotFoundException {
        Professor professor = this.professorRepository.findById(professorUsername)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor cannot be found"));
        JoinedSubject joinedSubject = this.joinedSubjectRepository.findById(joinedSubjectId)
                .orElseThrow(() -> new JoinedSubjectNotFoundException("Subject cannot be found"));
        Student student = this.studentRepository.findById(studentIndex)
                .orElseThrow(() -> new StudentNotFoundException("Student cannot be found"));
        DisciplinaryType category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DisciplinaryTypeNotFoundException("Category cannot be found"));
        return new Result(professor, joinedSubject, student, category);
    }

    private record Result(Professor professor, JoinedSubject joinedSubject, Student student, DisciplinaryType category) {
    }
}
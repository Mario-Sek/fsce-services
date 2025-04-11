package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.exceptions.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface DisciplinaryRecordService {

    DisciplinaryRecord createNewReport(Integer degree, String note, String professorUsername, String studentIndex,
                                       String CourseId, String categoryId, DisciplinaryStatus status, LocalDate date)
            throws StudentNotFoundException, ProfessorNotFoundException, DisciplinaryTypeNotFoundException, JoinedSubjectNotFoundException;

    Page<DisciplinaryRecord> findAllReports(DisciplinaryStatus status, String professor, String subject, Long meeting, String suggestedSanction, int pageNumber, int pageSize) throws ProfessorNotFoundException, JoinedSubjectNotFoundException;

    List<DisciplinaryRecord> findAllReportsForStudent(String index) throws StudentNotFoundException;



    DisciplinaryRecord findReportById(String id) throws DisciplinaryRecordNotFoundException;

    DisciplinaryRecord  updateReport(String reportId, Float degree, String note, String professorUsername, String studentIndex, String courseId,
                                     String categoryId, DisciplinaryStatus status, LocalDate date)
            throws DisciplinaryRecordNotFoundException, StudentNotFoundException, ProfessorNotFoundException,
            DisciplinaryTypeNotFoundException, JoinedSubjectNotFoundException;

    DisciplinaryRecord updateReportForStudent(String reportId, Boolean admitReport, String studentNote) throws DisciplinaryRecordNotFoundException;

    DisciplinaryRecord setSanctionForRecord(String id, String decisionName, String decisionDescription) throws DisciplinaryRecordNotFoundException;

    DisciplinaryRecord updateStudentNotification(String reportId) throws DisciplinaryRecordNotFoundException;

}

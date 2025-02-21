package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.base.ClassType;
import mk.ukim.finki.wp.fcseservices.model.base.Language;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import org.springframework.data.domain.Page;

public interface ProfessorEngagementService {
    ProfessorEngagement findById(String id);

    void addNew(Professor professor, String subjectId, ClassType classType,
                Float numberOfClasses, boolean otherTeacher, Language language,
                Short numberOfStudents, boolean consultative, String note,
                String semesterId);

    void update(String declarationId, Float numberOfClasses, boolean otherTeacher, Language language,
                Short numberOfStudents, boolean consultative, String note, String semesterId);

    void delete(String id);


    Page<ProfessorEngagement> findPage(String professor, String semester, String subject,
                                       Integer pageNum, Integer results);
}

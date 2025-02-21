package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorEngagementNotFound;
import mk.ukim.finki.wp.fcseservices.repository.*;
import mk.ukim.finki.wp.fcseservices.service.*;
import mk.ukim.finki.wp.fcseservices.service.validators.NumberOfClassesValidator;
import mk.ukim.finki.wp.fcseservices.service.validators.NumberOfStudentsValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static mk.ukim.finki.wp.fcseservices.service.specifications.FieldFilterSpecification.filterEquals;

@AllArgsConstructor
@Service
public class ProfessorEngagementServiceImpl implements ProfessorEngagementService {

    private final JoinedSubjectRepository joinedSubjectRepository;
    private final SemesterRepository semesterManagementRepository;
    private final ProfessorEngagementRepository engagementRepository;
    private final NumberOfStudentsValidator numberOfStudentsValidator;
    private final NumberOfClassesValidator numberOfClassesValidator;


    @Override
    public ProfessorEngagement findById(String id) {
        return engagementRepository.findById(id).orElseThrow(ProfessorEngagementNotFound::new);
    }

    @Override
    public void addNew(Professor professor, String subjectId, ClassType classType, Float numberOfClasses,
                       boolean otherTeacher, Language language, Short numberOfStudents, boolean consultative,
                       String note, String semesterId) {

        ProfessorEngagement engagement = new ProfessorEngagement(professor,
                semesterManagementRepository.getReferenceById(semesterId),
                joinedSubjectRepository.getReferenceById(subjectId),
                classType,
                language,
                numberOfClasses,
                otherTeacher,
                numberOfStudents,
                consultative,
                note,
                numberOfClasses,
                numberOfStudents
        );
        engagement.setValidationMessage("This is a new record.");
        engagementRepository.save(engagement);
    }

    @Override
    public void update(String id, Float numberOfClasses, boolean otherTeacher, Language language,
                       Short numberOfStudents, boolean consultative, String note, String semesterId) {

        ProfessorEngagement engagement = this.findById(id);

        StringBuilder validationMessage = new StringBuilder();
        validationMessage.append(numberOfStudentsValidator.validateAndUpdateValidationMessage(engagement, Float.valueOf(numberOfStudents)));
        validationMessage.append(numberOfClassesValidator.validateAndUpdateValidationMessage(engagement, numberOfClasses));

        engagement.setNumberOfClasses(numberOfClasses);
        engagement.setSharedWithOtherTeacher(otherTeacher);
        engagement.setLanguage(language);
        engagement.setNumberOfStudents(numberOfStudents);
        engagement.setConsultative(consultative);
        engagement.setNote(note);
        engagement.setSemester(semesterManagementRepository.getReferenceById(semesterId));
        engagement.setValidationMessage(validationMessage.toString());
        engagementRepository.save(engagement);
    }

    @Override
    public void delete(String id) {
        engagementRepository.deleteById(id);
    }

    @Override
    public Page<ProfessorEngagement> findPage(String professor, String semester, String subject,
                                              Integer pageNum, Integer results) {
        Specification<ProfessorEngagement> spec = Specification
                .where(filterEquals(ProfessorEngagement.class, "professor.id", professor))
                .and(filterEquals(ProfessorEngagement.class, "semester.code", semester))
                .and(filterEquals(ProfessorEngagement.class, "subject.abbreviation", subject));
        return engagementRepository.findAll(spec, PageRequest.of(pageNum - 1, results,
                Sort.by(Sort.Direction.DESC, "semester.startDate")
                        .and(Sort.by("professor.id", "subject.abbreviation", "classType"))));
    }
}

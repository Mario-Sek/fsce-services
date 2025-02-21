package mk.ukim.finki.wp.fcseservices.service.validators;

import lombok.AllArgsConstructor;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.TeacherSubjectRequestsRepository;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectAllocationValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class MultipleLecturesValidator implements TeacherSubjectAllocationValidator {
    private final TeacherSubjectRequestsRepository teacherSubjectRequestsRepository;

    @Override
    public String validateAndUpdateValidationMessage(TeacherSubjectAllocations allocation) {
        StringBuilder validationMessage = new StringBuilder();

        List<TeacherSubjectRequests> teacherSubjectRequests = teacherSubjectRequestsRepository.findByProfessorAndSubject(allocation.getProfessor(), allocation.getSubject());


        for (TeacherSubjectRequests teacherSubjectRequest : teacherSubjectRequests) {
            if (teacherSubjectRequest.getPreferMultipleGroups() == null || Boolean.FALSE.equals(teacherSubjectRequest.getPreferMultipleGroups())) {
                if (allocation.getNumberOfLectureGroups() > 1 || allocation.getNumberOfExerciseGroups() > 1) {
                    validationMessage.append("Professor does not prefer multiple groups, but they are assigned to either exercises or lectures.\n");
                }
            }
        }

        return validationMessage.toString();
    }
}

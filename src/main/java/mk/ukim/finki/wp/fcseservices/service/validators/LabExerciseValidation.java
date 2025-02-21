package mk.ukim.finki.wp.fcseservices.service.validators;

import lombok.AllArgsConstructor;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import mk.ukim.finki.wp.fcseservices.repository.CoursePreferenceRepository;
import mk.ukim.finki.wp.fcseservices.repository.TeacherSubjectRequestsRepository;
import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectAllocationValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class LabExerciseValidation implements TeacherSubjectAllocationValidator {
    private final TeacherSubjectRequestsRepository teacherSubjectRequestsRepository;
    private final CoursePreferenceRepository coursePreferenceRepository;

    @Override
    public String validateAndUpdateValidationMessage(TeacherSubjectAllocations allocation) {
        StringBuilder validationMessage = new StringBuilder();

        List<TeacherSubjectRequests> teacherSubjectRequests = teacherSubjectRequestsRepository.findByProfessorAndSubject(allocation.getProfessor(), allocation.getSubject());

        CoursePreference coursePreference = coursePreferenceRepository.findById(allocation.getSubject().getAbbreviation())
                .orElse(null);


        for (TeacherSubjectRequests teacherSubjectRequest : teacherSubjectRequests) {
            if (coursePreference != null && !coursePreference.isLabExercisesAsConsultations()) {
                if (allocation.getNumberOfLabGroups() > 0 && (Boolean.FALSE.equals(teacherSubjectRequest.getPreferLabExercises()) || teacherSubjectRequest.getPreferLabExercises() == null)) {
                    validationMessage.append("Professor does not prefer lab exercises, but lab exercises are allocated and they are not consultations.\n");
                }
            }
        }

        return validationMessage.toString();
    }
}
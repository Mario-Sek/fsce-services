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
public class AuditoriumExerciseValidation implements TeacherSubjectAllocationValidator {
    private final TeacherSubjectRequestsRepository teacherSubjectRequestsRepository;

    @Override
    public String validateAndUpdateValidationMessage(TeacherSubjectAllocations allocation) {
        StringBuilder validationMessage = new StringBuilder();

        if (allocation.getProfessor().getTitle().isProfessor()) {

            List<TeacherSubjectRequests> teacherSubjectRequests = teacherSubjectRequestsRepository.findByProfessorAndSubject(allocation.getProfessor(), allocation.getSubject());

            for (TeacherSubjectRequests teacherSubjectRequest : teacherSubjectRequests) {

                boolean preferAuditoriumExercises = teacherSubjectRequest.getPreferAuditoriumExercises() != null && !Boolean.FALSE.equals(teacherSubjectRequest.getPreferAuditoriumExercises());
                // Check if the professor doesn't prefer exercises, but exercises are allocated
                if (!preferAuditoriumExercises && allocation.getNumberOfExerciseGroups() > 0) {
                    validationMessage.append("Professor does not prefer exercises, but exercises are allocated.\n");
                }

            }

        }

        return validationMessage.toString();
    }
}

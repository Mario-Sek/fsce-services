package mk.ukim.finki.wp.fcseservices.service.validators;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import mk.ukim.finki.wp.fcseservices.repository.*;

import mk.ukim.finki.wp.fcseservices.service.TeacherSubjectAllocationValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class TeacherSubjectAllocationValidation implements TeacherSubjectAllocationValidator {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;


    @Override
    public String validateAndUpdateValidationMessage(TeacherSubjectAllocations allocation) {

        StringBuilder validationMessage = new StringBuilder();
        Professor keyProfessor = allocation.getProfessor();
        JoinedSubject keySubject = allocation.getSubject();
        List<Course> coursesByJoinedSubjectAbbreviation = courseRepository.findCoursesByJoinedSubject_Abbreviation(keySubject.getAbbreviation());

        double lectureGroups = 0.0;
        double exercisesGroups = 0.0;

        for (Course course : coursesByJoinedSubjectAbbreviation){

            String[] professorIds =course.getProfessors().split(";");
            String[] assistantIds =course.getAssistants().split(";");

            List<Professor> professors = new ArrayList<>();
            for(String professorId : professorIds)
                professors.add(professorRepository.getReferenceById(professorId));

            List<Professor> assistants = new ArrayList<>();
            for(String assistantId : assistantIds)
                assistants.add(professorRepository.getReferenceById(assistantId));

            if (professors.contains(keyProfessor))
                lectureGroups += 1.0/professors.size();
            if (assistants.contains(keyProfessor))
                exercisesGroups += 1.0/assistants.size();
        }

        if (allocation.getNumberOfLectureGroups() != lectureGroups)
            validationMessage.append(keyProfessor.getName()+"'s lecture group count for the course "+allocation.getSubjectId()+" is invalid\n");
        if (allocation.getNumberOfExerciseGroups() != exercisesGroups)
            validationMessage.append(keyProfessor.getName()+"'s exercise group count for the course "+allocation.getSubjectId()+" is invalid\n");

        return validationMessage.toString();
    }
}

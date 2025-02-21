package mk.ukim.finki.wp.fcseservices.service.validators;



import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import mk.ukim.finki.wp.fcseservices.service.ProfessorEngagementValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class NumberOfStudentsValidator implements ProfessorEngagementValidator {

    @Value("${studentsThreshold}")
    private int threshold;

    @Override
    public String validateAndUpdateValidationMessage(ProfessorEngagement engagement, Float newNumberOfStudents) {
        StringBuilder validationMessage = new StringBuilder();

        Short shortNewNumberOfStudents = (newNumberOfStudents != null) ? newNumberOfStudents.shortValue() : null;

        int difference = Math.abs(engagement.getNumberOfStudents() - shortNewNumberOfStudents);
        if (difference > threshold) {
            validationMessage.append("The new number of students differs by more than ").append(threshold)
                    .append(".\n");
        }

        return validationMessage.toString();
    }
}

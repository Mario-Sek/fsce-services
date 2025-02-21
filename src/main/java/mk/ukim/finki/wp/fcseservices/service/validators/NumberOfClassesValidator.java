package mk.ukim.finki.wp.fcseservices.service.validators;



import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;
import mk.ukim.finki.wp.fcseservices.service.ProfessorEngagementValidator;
import org.springframework.stereotype.Component;

@Component
public class NumberOfClassesValidator implements ProfessorEngagementValidator {

    @Override
    public String validateAndUpdateValidationMessage(ProfessorEngagement engagement, Float newNumberOfClasses) {
        StringBuilder validationMessage = new StringBuilder();

        Float currentNumberOfClasses = engagement.getNumberOfClasses();
        if (!currentNumberOfClasses.equals(newNumberOfClasses)) {
            validationMessage.append("The number of classes has been changed.\n");
        }
        return validationMessage.toString();
    }
}
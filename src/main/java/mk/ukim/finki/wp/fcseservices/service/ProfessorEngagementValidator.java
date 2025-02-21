package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.engagement.ProfessorEngagement;

public interface ProfessorEngagementValidator {
    String validateAndUpdateValidationMessage(ProfessorEngagement engagement, Float number);
}

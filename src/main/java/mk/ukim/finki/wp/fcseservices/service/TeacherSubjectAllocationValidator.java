package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectAllocations;

public interface TeacherSubjectAllocationValidator {
    String validateAndUpdateValidationMessage(TeacherSubjectAllocations allocation);
}

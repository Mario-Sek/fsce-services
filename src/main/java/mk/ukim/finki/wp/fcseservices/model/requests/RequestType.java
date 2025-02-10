package mk.ukim.finki.wp.fcseservices.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestType {
    GENERAL(GeneralStudentRequest.class),
    STUDY_PROGRAM_CHANGE(ChangeStudyProgramStudentRequest.class),
    COURSE_GROUP_CHANGE(CourseGroupChangeStudentRequest.class),
    LATE_COURSE_ENROLLMENT(LateCourseEnrollmentStudentRequest.class),
    COURSE_ENROLLMENT_WITHOUT_REQUIREMENTS(CourseEnrollmentWithoutRequirementsStudentRequest.class),
    PAYMENT_DISCOUNT_SINGLE_PARENT(GeneralStudentRequest.class),
    INSTALLMENT_PAYMENT(InstallmentPaymentStudentRequest.class);

    private final Class<? extends StudentRequest> studentRequestClass;
}

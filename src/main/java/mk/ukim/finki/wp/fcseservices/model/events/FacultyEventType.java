package mk.ukim.finki.wp.fcseservices.model.events;

public enum FacultyEventType {

    SEMESTER,
    SEMESTER_ENROLLMENT,
    EXAM_SESSION,
    EXAM_SESSION_ENROLLMENT,
    LECTURE,// not for now
    PRESENTATION, // Diploma start + 1h, Master start + 2h
    MEETING, // TeachingAndScientificMeeting +2h, DisciplinaryMeeting +1h
    CONSULTATION
}

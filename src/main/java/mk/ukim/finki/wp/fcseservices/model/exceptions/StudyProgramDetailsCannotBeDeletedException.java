package mk.ukim.finki.wp.fcseservices.model.exceptions;

public class StudyProgramDetailsCannotBeDeletedException extends RuntimeException {

    public StudyProgramDetailsCannotBeDeletedException(String id) {
        super("Study Program Details with id: " + id + " cannot be deleted");
    }
}

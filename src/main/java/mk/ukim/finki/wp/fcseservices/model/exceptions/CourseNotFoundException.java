package mk.ukim.finki.wp.fcseservices.model.exceptions;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException(String message) {
        super(message);
    }
}

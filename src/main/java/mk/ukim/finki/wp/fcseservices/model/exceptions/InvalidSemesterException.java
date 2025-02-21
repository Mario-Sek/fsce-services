package mk.ukim.finki.wp.fcseservices.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSemesterException extends RuntimeException {
    public InvalidSemesterException(String semesterCode) {
        super("Invalid semester code: " + semesterCode);
    }
}

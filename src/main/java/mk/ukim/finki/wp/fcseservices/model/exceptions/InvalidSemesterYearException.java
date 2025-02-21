package mk.ukim.finki.wp.fcseservices.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSemesterYearException extends RuntimeException {
    public InvalidSemesterYearException(String year) {
        super("Invalid semester year exception: " + year);
    }
}

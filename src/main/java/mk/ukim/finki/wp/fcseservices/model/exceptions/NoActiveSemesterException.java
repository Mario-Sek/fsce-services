package mk.ukim.finki.wp.fcseservices.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoActiveSemesterException extends RuntimeException {
    public NoActiveSemesterException() {
        super("There is no active semester");
    }
}

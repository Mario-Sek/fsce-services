package mk.ukim.finki.wp.fcseservices.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScientificProjectProgrammeNotFoundException extends RuntimeException {

    public ScientificProjectProgrammeNotFoundException() {
        super("Programme not found!");
    }
}

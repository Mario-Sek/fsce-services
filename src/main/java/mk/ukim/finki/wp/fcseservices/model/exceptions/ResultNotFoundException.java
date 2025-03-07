package mk.ukim.finki.wp.fcseservices.model.exceptions;

public class ResultNotFoundException extends RuntimeException {
    public ResultNotFoundException(Long id) {
        super(String.format("Result with id %d was not found", id));
    }
}

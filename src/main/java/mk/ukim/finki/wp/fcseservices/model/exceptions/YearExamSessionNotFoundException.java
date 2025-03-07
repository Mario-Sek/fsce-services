package mk.ukim.finki.wp.fcseservices.model.exceptions;

public class YearExamSessionNotFoundException extends RuntimeException {
    public YearExamSessionNotFoundException(String name) {
        super(String.format("YearExamSession with name %s was not found", name));
    }
}

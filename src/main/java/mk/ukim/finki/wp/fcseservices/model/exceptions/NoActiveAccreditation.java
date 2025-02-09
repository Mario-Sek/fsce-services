package mk.ukim.finki.wp.fcseservices.model.exceptions;

public class NoActiveAccreditation extends RuntimeException{
    public NoActiveAccreditation(String id) {
        super("No active accreditation");
    }
}

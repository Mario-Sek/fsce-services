package mk.ukim.finki.wp.fcseservices.model;

public enum AppRole {
    PROFESSOR, ADMIN, GUEST, STUDENT;


    public String roleName() {
        return "ROLE_" + this.name();
    }
}

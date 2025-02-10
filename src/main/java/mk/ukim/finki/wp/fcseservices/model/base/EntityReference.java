package mk.ukim.finki.wp.fcseservices.model.base;

import jakarta.persistence.Embeddable;

@Embeddable
public class EntityReference {

    private String referenceId;
    private String referenceClass;
}

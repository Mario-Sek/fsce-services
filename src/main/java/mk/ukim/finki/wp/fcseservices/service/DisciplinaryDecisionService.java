package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryRecordNotFoundException;

public interface DisciplinaryDecisionService {

    DisciplinaryRecord createOrUpdateDecision(String recordId, Long sanctionId, String description)
            throws DisciplinaryRecordNotFoundException;
}

package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryDecision;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinarySanction;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryStatus;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryRecordNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryDecisionRepository;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryRecordRepository;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinarySanctionRepository;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryDecisionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DisciplinaryDecisionServiceImpl implements DisciplinaryDecisionService {

    private final DisciplinaryRecordRepository disciplinaryRecordRepository;
    private final DisciplinarySanctionRepository disciplinarySanctionRepository;
    private final DisciplinaryDecisionRepository disciplinaryDecisionRepository;

    @Override
    @Transactional
    public DisciplinaryRecord createOrUpdateDecision(String recordId, Long sanctionId, String description)
            throws DisciplinaryRecordNotFoundException {

        DisciplinaryRecord record = disciplinaryRecordRepository.findAll()
                .stream()
                .filter(r -> r.getId().toString().equals(recordId))
                .findFirst()
                .orElseThrow(() -> new DisciplinaryRecordNotFoundException("Record not found with ID: " + recordId));

        DisciplinarySanction sanction = disciplinarySanctionRepository.findById(sanctionId)
                .orElseThrow(() -> new RuntimeException("Sanction not found with ID: " + sanctionId));

        DisciplinaryDecision decision;

        if (record.getDecision() != null) {
            decision = record.getDecision();
            decision.setSanction(sanction);
            decision.setDescription(description);
        } else {
            decision = new DisciplinaryDecision();
            decision.setRecord(record);
            decision.setSanction(sanction);
            decision.setDescription(description);
            decision = disciplinaryDecisionRepository.save(decision);
            record.setDecision(decision);
        }

        record.setStatus(DisciplinaryStatus.PROCESSED);

        return disciplinaryRecordRepository.save(record);
    }
}


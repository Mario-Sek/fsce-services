package mk.ukim.finki.wp.fcseservices.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mk.ukim.finki.wp.fcseservices.model.accreditations.Accreditation;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidAccreditation;
import mk.ukim.finki.wp.fcseservices.model.exceptions.NoActiveAccreditation;
import mk.ukim.finki.wp.fcseservices.repository.AccreditationRepository;
import mk.ukim.finki.wp.fcseservices.service.AccreditationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccreditationServiceImpl implements AccreditationService {
    private final AccreditationRepository accreditationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AccreditationServiceImpl(AccreditationRepository accreditationRepository) {
        this.accreditationRepository = accreditationRepository;
    }

    @Override
    public List<Accreditation> findAll() {
        return accreditationRepository.findAll();
    }

    @Override
    public Page<Accreditation> findAllWithPagination(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return accreditationRepository.findAll(pageRequest);
    }

    @Override
    public Accreditation findById(String year) {
        return accreditationRepository.findById(year).orElseThrow(() -> new InvalidAccreditation(year));
    }

//    @Override
//    public void deleteById(String year) {
//        accreditationRepository.deleteById(year);
//    }

    @Override
    @Transactional
    public void deleteById(String year) {

        entityManager.createNativeQuery(
                        "DELETE FROM accreditation_study_program_fields WHERE accreditation_year = ?1")
                .setParameter(1, year)
                .executeUpdate();


        entityManager.createNativeQuery(
                        "UPDATE subject_details SET accreditation_year = NULL WHERE accreditation_year = ?1")
                .setParameter(1, year)
                .executeUpdate();

        entityManager.createNativeQuery(
                        "UPDATE study_program_details SET accreditation_year = NULL WHERE accreditation_year = ?1")
                .setParameter(1, year)
                .executeUpdate();


        entityManager.createNativeQuery(
                        "DELETE FROM accreditation WHERE year = ?1")
                .setParameter(1, year)
                .executeUpdate();
    }

    @Override
    public Optional<Accreditation> save(String year, LocalDate activeFrom, LocalDate activeTo, List<String> studyProgramFields) {
        Accreditation accreditation;
        if (accreditationRepository.findById(year).isPresent()) {
            accreditation = accreditationRepository.findById(year).get();
            accreditation.setActiveFrom(activeFrom);
            accreditation.setActiveTo(activeTo);
        } else {
            accreditation = new Accreditation(year, activeFrom, activeTo, false, studyProgramFields);
        }
        return Optional.of(accreditationRepository.save(accreditation));
    }

    @Override
    public void activate(String year) {
        if (accreditationRepository.countAccreditationsByIsActiveTrue() > 0) {
            List<Accreditation> activeAccreditations = accreditationRepository.findAccreditationsByIsActiveTrue();
            for (Accreditation acc : activeAccreditations) {
                acc.setIsActive(false);
            }
            accreditationRepository.saveAll(activeAccreditations);
        } else {
            throw new NoActiveAccreditation(year);
        }

        if (accreditationRepository.findById(year).isPresent()) {
            Accreditation inactiveAccreditation = accreditationRepository.findById(year).get();
            inactiveAccreditation.setIsActive(true);
            accreditationRepository.save(inactiveAccreditation);
        } else throw new InvalidAccreditation(year);

    }

    @Override
    public Accreditation findActiveAccreditation() {
        return accreditationRepository.findAccreditationsByIsActiveTrue().get(0);
    }
}

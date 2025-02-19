package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.projects.GrantHolder;
import mk.ukim.finki.wp.fcseservices.model.exceptions.GrantHolderNotFound;
import mk.ukim.finki.wp.fcseservices.repository.GrantHolderRepository;
import mk.ukim.finki.wp.fcseservices.service.GrantHolderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrantHolderServiceImpl implements GrantHolderService {
    private final GrantHolderRepository grantHolderRepository;

    public GrantHolderServiceImpl(GrantHolderRepository grantHolderRepository) {
        this.grantHolderRepository = grantHolderRepository;
    }

    @Override
    public List<GrantHolder> findAll() {
        return grantHolderRepository.findAll();
    }

    @Override
    public Optional<GrantHolder> findById(Long id) {
        return grantHolderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        GrantHolder grantHolder = this.findById(id).orElseThrow(GrantHolderNotFound::new);
        this.grantHolderRepository.delete(grantHolder);
    }

    @Override
    public List<GrantHolder> findByLocation(String description) {
        return grantHolderRepository.findByDescription(description);
    }

    @Override
    public GrantHolder findByName(String name) {
        return grantHolderRepository.findByName(name);
    }

}

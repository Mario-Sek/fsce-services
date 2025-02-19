package mk.ukim.finki.wp.fcseservices.service.impl;


import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryType;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryTypeNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryTypeRepository;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaryTypeServiceImpl implements DisciplinaryTypeService {
    private final DisciplinaryTypeRepository disciplinaryTypeRepository;

    public DisciplinaryTypeServiceImpl(DisciplinaryTypeRepository disciplinaryTypeRepository) {
        this.disciplinaryTypeRepository = disciplinaryTypeRepository;
    }

    @Override
    public List<DisciplinaryType> findAllCategories() {
        return this.disciplinaryTypeRepository.findAll();
    }

    @Override
    public DisciplinaryType findById(String id) throws DisciplinaryTypeNotFoundException {
        return this.disciplinaryTypeRepository.findById(id).orElseThrow(() -> new DisciplinaryTypeNotFoundException("Category is not found"));
    }
}

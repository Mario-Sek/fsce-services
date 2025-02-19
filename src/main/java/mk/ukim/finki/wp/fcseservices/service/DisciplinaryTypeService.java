package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryType;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryTypeNotFoundException;

import java.util.List;


public interface DisciplinaryTypeService {

    List<DisciplinaryType> findAllCategories();
    DisciplinaryType findById(String id) throws DisciplinaryTypeNotFoundException;
}

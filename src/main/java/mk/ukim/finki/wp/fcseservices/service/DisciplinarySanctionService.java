package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinarySanction;

import java.util.List;

public interface DisciplinarySanctionService {

    List<DisciplinarySanction> findAllSanctions();
}

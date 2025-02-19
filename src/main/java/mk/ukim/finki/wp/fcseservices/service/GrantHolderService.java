package mk.ukim.finki.wp.fcseservices.service;


import mk.ukim.finki.wp.fcseservices.model.projects.GrantHolder;

import java.util.List;
import java.util.Optional;

public interface GrantHolderService {
    List<GrantHolder> findAll();

    Optional<GrantHolder> findById(Long id);

    void delete(Long id);

    List<GrantHolder> findByLocation(String location);

    GrantHolder findByName(String name);

}

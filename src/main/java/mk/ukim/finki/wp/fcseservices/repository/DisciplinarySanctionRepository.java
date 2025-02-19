package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinarySanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinarySanctionRepository extends JpaRepository<DisciplinarySanction, Long> {
    DisciplinarySanction findByName(String name);
}

package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.projects.GrantHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrantHolderRepository extends JpaRepository<GrantHolder, Long> {

    List<GrantHolder> findByDescription(String description);

    GrantHolder findByName(String name);
}

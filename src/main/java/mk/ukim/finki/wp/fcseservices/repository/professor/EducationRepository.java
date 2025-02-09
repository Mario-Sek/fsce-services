package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.professor.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, String> {
    void deleteAllByIdIn(List<String> ids);

}

package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherAllocationStats;
import org.springframework.stereotype.Repository;


@Repository
public interface TeacherAllocationStatsRepository extends JpaSpecificationRepository<TeacherAllocationStats, String> {

}


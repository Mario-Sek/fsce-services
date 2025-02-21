package mk.ukim.finki.wp.fcseservices.repository;


import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSubjectRequestsRepository extends JpaSpecificationRepository<TeacherSubjectRequests, Long> {

    @EntityGraph(attributePaths = {"professor", "subject"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("FROM TeacherSubjectRequests tsr inner join tsr.professor inner join tsr.subject")
    Page<TeacherSubjectRequests> findAll(Pageable pageable);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    Page<TeacherSubjectRequests> findByProfessorAndSubject(Professor professor, JoinedSubject subject, Pageable pageable);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    Page<TeacherSubjectRequests> findByProfessorId(String professorId, Pageable pageable);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    Page<TeacherSubjectRequests> findBySubject(JoinedSubject subject, Pageable pageable);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    List<TeacherSubjectRequests> findBySubject(JoinedSubject subject);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    TeacherSubjectRequests findFirstByProfessorAndSubject(Professor professor, JoinedSubject subject);

    @EntityGraph(value = "TeacherSubjectRequests.all", type = EntityGraph.EntityGraphType.LOAD)
    List<TeacherSubjectRequests> findByProfessorAndSubject(Professor professor, JoinedSubject subject);


}

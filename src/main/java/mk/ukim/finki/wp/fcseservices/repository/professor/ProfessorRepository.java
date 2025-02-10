package mk.ukim.finki.wp.fcseservices.repository.professor;

import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.dto.ProfessorNameAndCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProfessorRepository extends JpaRepository<Professor, String> {
    @Query("SELECT p FROM Professor p " + "WHERE (:title IS NULL OR p.title = :title) " + "AND (:stringSearch = '' OR p.name ILIKE %:stringSearch% " + "OR p.email ILIKE %:stringSearch%)")
    Page<Professor> findAllFiltered(@Param("stringSearch") String stringSearch, @Param("title") ProfessorTitle title, Pageable pageable);

    Page<Professor> findAll(Pageable pageable);

    @Query("SELECT new mk.ukim.finki.wp.fcseservices.model.dto.ProfessorNameAndCodeDTO(" +
            "p.id, " +
            "p.name) " +
            "FROM Professor p ")
    List<ProfessorNameAndCodeDTO> findAllNameAndCode();



}

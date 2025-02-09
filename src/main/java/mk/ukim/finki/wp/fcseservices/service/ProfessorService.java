package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.professor.Professor;
import mk.ukim.finki.wp.fcseservices.model.professor.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.professor.dto.ProfessorNameAndCodeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfessorService {

    Professor getProfessorById(String professorId);

    List<ProfessorNameAndCodeDTO> findAllProfessorNameAndCode();

    Page<Professor> findAllWithPagination(int pageNum, int pageSize);

    Page<Professor> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredTitle);

    Professor save(String id, String name, String email, ProfessorTitle title, Short orderingRank);

    void deleteById(String id);

    List<Professor> findAll();

}

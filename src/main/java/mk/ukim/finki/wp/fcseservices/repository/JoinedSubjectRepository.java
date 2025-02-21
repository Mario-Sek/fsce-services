package mk.ukim.finki.wp.fcseservices.repository;

import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JoinedSubjectRepository extends JpaSpecificationRepository<JoinedSubject, String> {
    JoinedSubject findByName(String name);

    JoinedSubject findByAbbreviation(String abbreviation);


    Page<JoinedSubject> findBySemesterType(SemesterType semesterType, Pageable pageable);

    @Query(value = "SELECT distinct js.* " +
            "FROM joined_subject js " +
            "LEFT JOIN teacher_subject_requests tsa ON js.abbreviation=tsa.subject_id " +
            "LEFT JOIN study_program_subject sps ON js.main_subject_id=sps.subject_id " +
            "WHERE tsa.id IS NOT NULL OR sps.mandatory = TRUE", nativeQuery = true)
    Page<JoinedSubject> findActivatedSubjects(Pageable pageable);


    //filter active subjects by name
    @Query(value = "SELECT distinct js.* " +
            "FROM joined_subject js " +
            "LEFT JOIN teacher_subject_requests tsa ON js.abbreviation=tsa.subject_id " +
            "LEFT JOIN study_program_subject sps ON js.main_subject_id=sps.subject_id " +
            "WHERE (tsa.id IS NOT NULL OR sps.mandatory = TRUE) AND (js.name like %:name% OR js.codes like %:name%)", nativeQuery = true)
    Page<JoinedSubject> findActivatedSubjectsByName(Pageable pageable, @Param("name") String name);


    //filter active subjects by semester type (winter/summer)
    @Query(value = "SELECT distinct js.* " +
            "FROM joined_subject js " +
            "LEFT JOIN teacher_subject_requests tsa ON js.abbreviation=tsa.subject_id " +
            "LEFT JOIN study_program_subject sps ON js.main_subject_id=sps.subject_id " +
            "WHERE (tsa.id IS NOT NULL OR sps.mandatory = TRUE) AND js.semester_type=:semesterType", nativeQuery = true)
    Page<JoinedSubject> findActivatedSubjectsBySemesterType(Pageable pageable, @Param("semesterType") String semesterType);


    //filter active subjects by name and semester type
    @Query(value = "SELECT distinct js.* " +
            "FROM joined_subject js " +
            "LEFT JOIN teacher_subject_requests tsa ON js.abbreviation=tsa.subject_id " +
            "LEFT JOIN study_program_subject sps ON js.main_subject_id=sps.subject_id " +
            "WHERE (tsa.id IS NOT NULL OR sps.mandatory = TRUE) AND  (js.name like %:name% OR js.codes like %:name%) AND js.semester_type=:semesterType", nativeQuery = true)
    Page<JoinedSubject> findActivatedSubjectsByNameAndSemesterType(Pageable pageable,
                                                                   @Param("name") String name,
                                                                   @Param("semesterType") String semesterType);
}

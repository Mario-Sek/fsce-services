package mk.ukim.finki.wp.fcseservices.model.teachingallocation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/**
 * create view subject_needs_view
 * (id,
 * semester_code,
 * subject_name,
 * n_groups,
 * n_assigned_professors,
 * n_req_professors,
 * n_assigned_assistants,
 * n_req_assistants,
 * n_calc_groups,
 * n_covered_groups)
 */
@Entity
@Table(name = "subject_needs_view")
@Immutable
@Getter
@Setter
@NoArgsConstructor
public class SubjectNeedsView {

    @Id
    private String id;

    private String semesterCode;

    private String subjectName;

    private Integer nGroups;

    private Integer nCalcGroups;

    private Integer nAssignedProfessors;

    private Integer nReqProfessors;

    private Integer nAssignedAssistants;

    private Integer nReqAssistants;

    private Integer nCalcLabGroups;

    private Integer nCoveredLabGroups;


}

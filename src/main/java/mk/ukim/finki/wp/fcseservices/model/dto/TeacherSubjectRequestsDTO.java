package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"acceptsEnglishGroup", "preferAuditoriumExercises", "preferLabExercises", "preferMultipleGroups", "priority", "professorId", "startedExerciseFromSemesterCode", "startedTeachingFromSemesterCode", "subjectName"})
public class TeacherSubjectRequestsDTO {
    private String professorId;
    private String subjectAbbreviation;
    private String subjectName;
    private Double priority;
    private Boolean preferMultipleGroups;
    private Boolean preferAuditoriumExercises;
    private Boolean preferLabExercises;
    private String startedTeachingFromSemesterCode;
    private String startedExerciseFromSemesterCode;
    private Boolean acceptsEnglishGroup;
}

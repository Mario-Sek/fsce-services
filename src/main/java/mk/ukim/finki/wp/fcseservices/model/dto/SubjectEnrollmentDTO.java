package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"index", "code", "numEnrollments", "professor", "subjectName", "message"})
public class SubjectEnrollmentDTO {
    private String index;
    private String code;
    private Integer numEnrollments;
    private String professor;
    private String subjectName;
    private String message;
}

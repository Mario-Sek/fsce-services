package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"abbreviation", "name", "codes", "semesterType", "mainSubject", "weeklyLecturesClasses", "weeklyAuditoriumClasses", "weeklyLabClasses", "cycle", "validationMessage"})
public class JoinedSubjectDTO {
    private String abbreviation;
    private String name;
    private String codes;
    private String semesterType;
    private String mainSubjectCode;
    private Integer weeklyLecturesClasses;
    private Integer weeklyAuditoriumClasses;
    private Integer weeklyLabClasses;
    private String cycle;
    private String validationMessage;
}

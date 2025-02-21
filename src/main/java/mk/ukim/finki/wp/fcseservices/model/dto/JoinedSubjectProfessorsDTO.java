package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;


import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"subject", "professors"})
public class JoinedSubjectProfessorsDTO {
    private JoinedSubject subject;
    private List<Professor> professors;

    public String professorsToString() {
        return professors.stream()
                .map(Professor::getName)
                .collect(Collectors.joining(", "));
    }
}

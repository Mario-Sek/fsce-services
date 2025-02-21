package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.TeacherSubjectRequests;


import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"subject", "teacherSubjectRequests"})
public class JoinedSubjectTeacherRequestDTO {
    private JoinedSubject subject;
    private List<TeacherSubjectRequests> teacherSubjectRequests;

}

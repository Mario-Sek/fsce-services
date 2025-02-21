package mk.ukim.finki.wp.fcseservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherSubjectAllocationsDTO {

    private String professor;
    private String subject;
    private Float numberOfLectureGroups;
    private Float numberOfExerciseGroups;
    private Float numberOfLabGroups;

}

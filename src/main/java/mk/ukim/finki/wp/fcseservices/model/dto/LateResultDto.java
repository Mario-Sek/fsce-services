package mk.ukim.finki.wp.fcseservices.model.dto;

import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LateResultDto {

    private String professorId;
    private String professorName;

    private String subjectName;

    private LocalDateTime examDate;

    private LocalDateTime uploadDate;

    private String sessionName;







}

package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"name", "lectureSharing", "auditoriumExercisesSharing","preferOnlineLectures","preferOnlineExercises","labExercisesAsConsultations","message"})
public class CoursePreferenceDTO {
    private String name;
    private String lectureSharing;
    private String auditoriumExercisesSharing;
    private boolean preferOnlineLectures;
    private boolean preferOnlineExercises;
    private boolean labExercisesAsConsultations;
    private String message;
}

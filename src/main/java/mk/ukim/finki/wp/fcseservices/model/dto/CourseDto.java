package mk.ukim.finki.wp.fcseservices.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.CoursePreference;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.LectureSharing;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"semesterCode", "joinedSubject", "subjectName", "professors", "assistants",
        "numberOfFirstEnrollments", "numberOfReEnrollments", "groupPortion",
        "groups", "english"})
public class CourseDto {

    private String semesterCode;
    private String joinedSubject;
    private String subjectName;
    private String professors;
    private String assistants;
    private Integer numberOfFirstEnrollments;
    private Integer numberOfReEnrollments;
    private Float groupPortion = 1.0F;
    private String groups;
    private Boolean english = false;
    private LectureSharing lectureSharing;
    private LectureSharing auditoriumExercisesSharing;
    private String message;

    public CourseDto(Course course, CoursePreference preference) {
        this.semesterCode = course.getSemester().getCode();
        this.joinedSubject = course.getJoinedSubject().getAbbreviation();
        this.subjectName = course.getJoinedSubject().getName();
        this.professors = course.getProfessors();
        this.assistants = course.getAssistants();
        this.numberOfFirstEnrollments = course.getNumberOfFirstEnrollments();
        this.numberOfReEnrollments = course.getNumberOfReEnrollments();
        this.groupPortion = course.getGroupPortion();
        this.groups = course.getGroups();
        if (preference != null) {
            this.lectureSharing = preference.getLectureSharing();
            this.auditoriumExercisesSharing = preference.getAuditoriumExercisesSharing();
        }
        this.english = course.getEnglish();
    }
}

package mk.ukim.finki.wp.fcseservices.model.results;

import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultsResponse {

    private long id;

    private YearExamSession session;

    private Course course;

    private String pdf;
}

package mk.ukim.finki.wp.fcseservices.model.examschedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class YearExamSession {

    // 2022-23-JUNE
    @Id
    private String name;

    @Enumerated(EnumType.STRING)
    private ExamSession session;

    // 2022-23
    private String year;

    private LocalDate sessionStart;

    private LocalDate sessionEnd;

    private LocalDate enrollmentStartDate;

    private LocalDate enrollmentEndDate;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<StudyCycle> cycle;


    public YearExamSession(ExamSession session, String year) {
        this.name = String.format("%s-%s", year, session.name());
        this.session = session;
        this.year = year;
    }


    public YearExamSession(ExamSession session, String year, LocalDate sessionStart, LocalDate sessionEnd, LocalDate enrollmentStartDate, LocalDate enrollmentEndDate, List<StudyCycle> cycle) {
        this.name = String.format("%s-%s", year, session.name());
        this.session = session;
        this.year = year;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.enrollmentStartDate = enrollmentStartDate;
        this.enrollmentEndDate = enrollmentEndDate;
        this.cycle = cycle;
    }
}

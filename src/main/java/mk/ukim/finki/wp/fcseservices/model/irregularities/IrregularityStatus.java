package mk.ukim.finki.wp.fcseservices.model.irregularities;

import lombok.Getter;

@Getter
public enum IrregularityStatus {
    UNREAD("Не прочитано"),
    READ ("Прочитано"),
    DONE("Разрешено");

    private final String macedonianName;

    IrregularityStatus(String macedonianName) {
        this.macedonianName = macedonianName;
    }
}

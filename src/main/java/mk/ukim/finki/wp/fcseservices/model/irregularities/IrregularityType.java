package mk.ukim.finki.wp.fcseservices.model.irregularities;

import lombok.Getter;

@Getter
public enum IrregularityType {
    SUBJECT("Предмет"),
    PROFESSOR("Професор"),
    STUDENT("Студент"),
    ROOM("Соба"),
    GENERAL("Генерално"),
    ADMINISTRATION("Администрација");

    private final String macedonianName;

    IrregularityType(String macedonianName) {
        this.macedonianName = macedonianName;
    }

}
package mk.ukim.finki.wp.fcseservices.model.lostfound;

import lombok.Getter;

@Getter
public enum ItemType {
    LOST("Изгубено"),
    FOUND("Најдено");

    private final String typeName;

    ItemType(String typeName) {
        this.typeName = typeName;
    }

}

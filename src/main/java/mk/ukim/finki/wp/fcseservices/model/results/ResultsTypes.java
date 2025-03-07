package mk.ukim.finki.wp.fcseservices.model.results;

import java.util.Arrays;

public enum ResultsTypes {
    SUMARNI("Сумарни"),
    TEORIJA("Теорија"),
    PRAKTICNO("Практично"),
    LABORATORISKI("Лабораториски"),
    TEST("Тест"),
    KVALIFIKACISKI_TEST("Квалификациски тест"),
    PROEKT("Проект");

    private final String translatedName;

    ResultsTypes(String translatedName) {
        this.translatedName = translatedName;
    }

    public String getTranslatedName() {
        return translatedName;
    }

    @Override
    public String toString() {
        return translatedName;
    }
    public static String[] getAllTypesTranslated(){
       return Arrays.stream(ResultsTypes.values()).map(ResultsTypes::getTranslatedName).toArray(String[]::new);
    }
}

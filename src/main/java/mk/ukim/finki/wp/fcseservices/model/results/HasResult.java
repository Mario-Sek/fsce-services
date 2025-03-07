package mk.ukim.finki.wp.fcseservices.model.results;

import java.util.Arrays;

public enum HasResult {
    Any,
    Yes,
    No;

    public static String[] getAll(){
        return Arrays.stream(HasResult.values()).map(Enum::name).toArray(String[]::new);
    }
}

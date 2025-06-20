package backend.main.utils;

import java.util.regex.Pattern;

public class EmailUtil {
    public static boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(regex, email);
    }
}

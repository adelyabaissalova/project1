package util;

public class ValidationUtils {
    public static boolean isPositiveId(int id) {
        return id > 0;
    }

    public static boolean isNonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (!isNonBlank(email)) return false;
        return email.contains("@") && email.contains(".");
    }

    public static String normalize(String s) {
        return s == null ? null : s.trim();
    }
}

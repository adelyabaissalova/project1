package util;

public class Validator {

    // Title should not be null and at least 2 characters long
    public static boolean isValidTitle(String title) {
        return title != null && title.trim().length() >= 2;
    }

    // ID must be positive
    public static boolean isValidId(int id) {
        return id > 0;
    }

    // Checks if a string is not null, not empty, and not only whitespace
    public static boolean isNonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // Normalize string: trim and remove extra spaces
    public static String normalize(String s) {
        if (s == null) return null;
        return s.trim().replaceAll("\\s+", " ");
    }

    // Another method for checking positive IDs (can be same as isValidId)
    public static boolean isPositiveId(int id) {
        return id > 0;
    }
}


package util;

public class Validator {

    public static boolean isNonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPositiveId(int id) {
        return id > 0;
    }

    public static String normalize(String s) {
        if (s == null) return null;
        return s.trim();
    }

    public static boolean isValidTitle(String title) {
        if (!isNonBlank(title)) return false;
        int n = title.trim().length();
        return n >= 2 && n <= 150;
    }

    public static boolean isValidName(String name) {
        if (!isNonBlank(name)) return false;
        int n = name.trim().length();
        return n >= 2 && n <= 50;
    }

    public static boolean isValidGenre(String genre) {
        if (!isNonBlank(genre)) return false;
        int n = genre.trim().length();
        return n >= 2 && n <= 50;
    }
}

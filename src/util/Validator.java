package util;

public class Validator {
    public static void requireNotBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(field + " cannot be empty");
    }

    public static int requirePositiveInt(String value, String field) {
        try {
            int x = Integer.parseInt(value);
            if (x <= 0) throw new IllegalArgumentException(field + " must be > 0");
            return x;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " must be a number");
        }
    }
}

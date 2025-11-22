package auth;

import android.util.Patterns;

public class ValidationUtils {

    // Validate email format
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validate password length (you can increase rules later)
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Check if any string is empty
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String... values) {
        {
            for (String value : values) {
                if (isEmpty(value)) {
                    return false;
                }
            }
            return true;
        }}

    // Validate phone number
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 10 && Patterns.PHONE.matcher(phone).matches();
    }

    // Validate basic text fields like name, city, title
    public static boolean isValidText(String value) {
        return value != null && value.trim().length() >= 3;
    }

    // Validate price (must be a number)
    public static boolean isValidPrice(String price) {
        try {
            double p = Double.parseDouble(price);
            return p > 0;
        } catch (Exception e) {
            return false;
        }
    }
}

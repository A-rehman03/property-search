package auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import database.DatabaseHelper;
import models.User;

public class UserAuthService {

    private DatabaseHelper dbHelper;

    public UserAuthService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ------------------------------
    // SIGNUP
    // ------------------------------
    public boolean registerUser(User user) {

        // SAFETY CHECK: Never allow null or empty email/username/password
        if (user == null ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            Log.d("SIGNUP_DEBUG", "User details - " +
                    "username: " + user.getUsername() +
                    ", email: " + user.getEmail() +
                    ", password: " + user.getPassword() +
                    ", phone: " + user.getPhoneNumber() +
                    ", role: " + user.getRole());
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if email already exists
        if (isEmailTaken(user.getEmail())) {
            Log.d("SIGNUP_DEBUG", "Email already exists: " + user.getEmail());
            db.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        values.put("phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "");

        // --- LOGGING BEFORE INSERT ---
        Log.d("SIGNUP_DEBUG", "Attempting to insert user: " + values.toString());

        long result = db.insert("users", null, values);

        // --- LOGGING AFTER INSERT ---
        Log.d("SIGNUP_DEBUG", "User details - username: " + user.getUsername()
                + ", email: " + user.getEmail()
                + ", password: " + user.getPassword()
                + ", phone: " + user.getPhoneNumber()
                + ", role: " + user.getRole());

        db.close();

        return result != -1;
    }

    // ------------------------------
    // CHECK IF EMAIL EXISTS
    // ------------------------------
    public boolean isEmailTaken(String email) {

        // 🛑 Fix: Prevent NULL → rawQuery crash
        if (email == null || email.trim().isEmpty()) {
            return false;   // Empty email cannot be "taken"
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email = ?",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    // ------------------------------
    // LOGIN
    // ------------------------------
    public User login(String email, String password) {

        if (email == null || password == null) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        if (cursor.moveToFirst()) {
            User user = new User();

            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));

            cursor.close();
            db.close();
            return user;
        }

        cursor.close();
        db.close();
        return null;
    }

    // ------------------------------
    public boolean isLoggedIn() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE isLoggedIn = 1", null);
        boolean isLoggedIn = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isLoggedIn;
    }

    public Object getLoggedInUserRole() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE isLoggedIn = 1", null);

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            db.close();
            return role;
        }

        cursor.close();
        db.close();
        return null;
    }

    public boolean register(String username, String email, String password, String phone, String role) {

        if (email == null || email.trim().isEmpty()) return false;

        User user = new User(username, email, password, phone, role);
        return registerUser(user);
    }
}

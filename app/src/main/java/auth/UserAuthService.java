package auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if email already exists
        if (isEmailTaken(user.getEmail())) {
            return false;  // Email already exists
        }

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        values.put("phoneNumber", user.getPhoneNumber());

        long result = db.insert("users", null, values);
        db.close();

        return result != -1;
    }

    // Check if email exists
    public boolean isEmailTaken(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email = ?",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }


    // ------------------------------
    // LOGIN
    // ------------------------------
    public User login(String email, String password) {
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
        return null; // No match found
    }
}

package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import models.Property;
import models.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "realestate.db";
    public static final int DATABASE_VERSION = 2;

    // Tables
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PROPERTIES = "properties";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "phoneNumber TEXT, " +
                "isLoggedIn INTEGER DEFAULT 0" +
                ");"
        );


        db.execSQL("CREATE TABLE " + TABLE_PROPERTIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "adminId INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "location TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "imagePaths TEXT, " +
                "datePosted TEXT, " +
                "phoneNumber TEXT, " +
                "FOREIGN KEY(adminId) REFERENCES users(id)" +
                ");"
        );

        db.execSQL("CREATE INDEX idx_location ON properties(location);");
        db.execSQL("CREATE INDEX idx_type ON properties(type);");
        db.execSQL("CREATE INDEX idx_price ON properties(price);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    // ----------------------------
    // LOGIN USER
    // ----------------------------
    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        if (cursor != null && cursor.moveToFirst()) {

            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("role")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            );

            cursor.close();
            db.close();
            return user;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }


    // ----------------------------
    // GET ALL PROPERTIES
    // ----------------------------
    public ArrayList<Property> getAllProperties() {
        ArrayList<Property> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROPERTIES, null);

        if (cursor.moveToFirst()) {
            do {
                Property p = new Property(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("adminId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("imagePaths")),
                        cursor.getString(cursor.getColumnIndexOrThrow("datePosted")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                );

                list.add(p);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }


    // ----------------------------
    // GET SINGLE PROPERTY
    // ----------------------------
    public Property getPropertyById(int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PROPERTIES + " WHERE id = ?",
                new String[]{String.valueOf(propertyId)}
        );

        if (cursor != null && cursor.moveToFirst()) {

            Property p = new Property(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("adminId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("location")),
                    cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("imagePaths")),
                    cursor.getString(cursor.getColumnIndexOrThrow("datePosted")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            );

            cursor.close();
            db.close();
            return p;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public long addProperty(Property p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("adminId", p.getAdminId());
        values.put("title", p.getTitle());
        values.put("description", p.getDescription());
        values.put("price", p.getPrice());
        values.put("location", p.getLocation());
        values.put("type", p.getType());
        values.put("imagePaths", p.getImagePaths());
        values.put("datePosted", p.getDatePosted());
        values.put("phoneNumber", p.getPhoneNumber());

        long result = db.insert("properties", null, values);
        db.close();
        return result;
    }

}

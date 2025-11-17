package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "realestate.db";
    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PROPERTIES = "properties";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +               // admin or user
                "phoneNumber TEXT" +
                ");"
        );

        // Create Properties Table
        db.execSQL("CREATE TABLE " + TABLE_PROPERTIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "adminId INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "location TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "imagePaths TEXT, " +                  // comma-separated image URIs
                "datePosted TEXT, " +
                "phoneNumber TEXT, " +
                "FOREIGN KEY(adminId) REFERENCES users(id)" +
                ");"
        );

        // Indexes for faster search
        db.execSQL("CREATE INDEX idx_location ON properties(location);");
        db.execSQL("CREATE INDEX idx_type ON properties(type);");
        db.execSQL("CREATE INDEX idx_price ON properties(price);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate (simple development strategy)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}

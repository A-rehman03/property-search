package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import models.Property;
import models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "real_estate.db";
    private static final int DATABASE_VERSION = 4; // Incremented version to trigger onUpgrade

    // User table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";
    private static final String COLUMN_USER_PHONE = "phoneNumber";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_IS_LOGGED_IN = "isLoggedIn";

    // Property table
    private static final String TABLE_PROPERTIES = "properties";
    private static final String COLUMN_PROPERTY_ID = "property_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_IMAGE_URL = "image_url"; // Will store a single representative image URL
    private static final String COLUMN_ADMIN_ID = "admin_id";

    // Image table
    private static final String TABLE_PROPERTY_IMAGES = "property_images";
    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_PROPERTY_FK_ID = "property_id";

    // Favorites table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_FAVORITE_ID = "favorite_id";
    private static final String COLUMN_USER_FK_ID = "user_id";
    private static final String COLUMN_PROPERTY_FAVORITE_FK_ID = "property_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_IS_LOGGED_IN + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PROPERTIES_TABLE = "CREATE TABLE " + TABLE_PROPERTIES + "("
                + COLUMN_PROPERTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_PHONE_NUMBER + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_ADMIN_ID + " INTEGER" + ")";
        db.execSQL(CREATE_PROPERTIES_TABLE);

        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_PROPERTY_IMAGES + "("
                + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_IMAGE_PATH + " TEXT NOT NULL,"
                + COLUMN_PROPERTY_FK_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_PROPERTY_FK_ID + ") REFERENCES " + TABLE_PROPERTIES + "(" + COLUMN_PROPERTY_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_IMAGES_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_FK_ID + " INTEGER,"
                + COLUMN_PROPERTY_FAVORITE_FK_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_FK_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_PROPERTY_FAVORITE_FK_ID + ") REFERENCES " + TABLE_PROPERTIES + "(" + COLUMN_PROPERTY_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTY_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public User loginUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_ROLE, COLUMN_USER_PHONE, COLUMN_USER_NAME};
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, pass};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;

        }
    }
}

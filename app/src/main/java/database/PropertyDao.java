package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import models.Property;

import java.util.ArrayList;

public class PropertyDao {

    private DatabaseHelper dbHelper;

    public PropertyDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ---------------------------------------------
    // ADD PROPERTY
    // ---------------------------------------------
    public long addProperty(Property property) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("adminId", property.getAdminId());
        values.put("title", property.getTitle());
        values.put("description", property.getDescription());
        values.put("price", property.getPrice());
        values.put("location", property.getLocation());
        values.put("type", property.getType());
        values.put("imagePaths", property.getImagePaths());
        values.put("datePosted", property.getDatePosted());
        values.put("phoneNumber", property.getPhoneNumber());

        long id = db.insert("properties", null, values);
        db.close();
        return id;
    }

    // ---------------------------------------------
    // UPDATE PROPERTY
    // ---------------------------------------------
    public boolean updateProperty(Property property) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", property.getTitle());
        values.put("description", property.getDescription());
        values.put("price", property.getPrice());
        values.put("location", property.getLocation());
        values.put("type", property.getType());
        values.put("imagePaths", property.getImagePaths());
        values.put("datePosted", property.getDatePosted());
        values.put("phoneNumber", property.getPhoneNumber());

        int rows = db.update("properties", values, "id = ?", new String[]{String.valueOf(property.getId())});
        db.close();

        return rows > 0;
    }

    // ---------------------------------------------
    // DELETE PROPERTY
    // ---------------------------------------------
    public boolean deleteProperty(int propertyId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("properties", "id = ?", new String[]{String.valueOf(propertyId)});
        db.close();
        return rows > 0;
    }

    // ---------------------------------------------
    // GET ALL PROPERTIES (for users)
    // ---------------------------------------------
    public ArrayList<Property> getAllProperties() {
        ArrayList<Property> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM properties ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ---------------------------------------------
    // GET PROPERTIES OF A SPECIFIC ADMIN
    // ---------------------------------------------
    public ArrayList<Property> getPropertiesByAdmin(int adminId) {
        ArrayList<Property> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM properties WHERE adminId = ? ORDER BY id DESC",
                new String[]{String.valueOf(adminId)}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ---------------------------------------------
    // GET A SINGLE PROPERTY BY ID
    // ---------------------------------------------
    public Property getPropertyById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM properties WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            Property property = cursorToProperty(cursor);
            cursor.close();
            db.close();
            return property;
        }

        cursor.close();
        db.close();
        return null;
    }

    // ---------------------------------------------
    // SEARCH PROPERTY (Title, Location)
    // ---------------------------------------------
    public ArrayList<Property> searchProperties(String keyword) {
        ArrayList<Property> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM properties WHERE title LIKE ? OR location LIKE ? ORDER BY id DESC",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ---------------------------------------------
    // FILTER BY TYPE & PRICE RANGE & LOCATION
    // ---------------------------------------------
    public ArrayList<Property> filterProperties(String type, double minPrice, double maxPrice, String location) {
        ArrayList<Property> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM properties WHERE type = ? AND price BETWEEN ? AND ? AND location LIKE ? ORDER BY id DESC",
                new String[]{type, String.valueOf(minPrice), String.valueOf(maxPrice), "%" + location + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ---------------------------------------------
    // HELPER: MAP CURSOR → PROPERTY MODEL
    // ---------------------------------------------
    private Property cursorToProperty(Cursor cursor) {
        Property p = new Property();

        p.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        p.setAdminId(cursor.getInt(cursor.getColumnIndexOrThrow("adminId")));
        p.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        p.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        p.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
        p.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
        p.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        p.setImagePaths(cursor.getString(cursor.getColumnIndexOrThrow("imagePaths")));
        p.setDatePosted(cursor.getString(cursor.getColumnIndexOrThrow("datePosted")));
        p.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));

        return p;
    }
}

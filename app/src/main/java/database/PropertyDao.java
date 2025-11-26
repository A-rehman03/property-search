package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import models.Property;

public class PropertyDao {

    public static final String COLUMN_PROPERTY_ID = "property_id";
    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_TYPE = "type";
    public static final String TABLE_PROPERTIES = "properties";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    private DatabaseHelper dbHelper;

    public PropertyDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addProperty(Property property) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long propertyId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("title", property.getTitle());
            values.put("description", property.getDescription());
            values.put("price", property.getPrice());
            values.put("address", property.getAddress());
            values.put("type", property.getType());
            values.put("phone_number", property.getPhoneNumber());
            values.put("admin_id", property.getAdminId());

            // The 'image_url' column in the properties table is redundant.
            // The separate 'property_images' table is the correct source of truth.
            // This line has been removed to prevent data duplication and potential bugs.
            // if (property.getImagePaths() != null && !property.getImagePaths().isEmpty()) {
            //     values.put("image_url", property.getImagePaths().get(0));
            // }

            propertyId = db.insert("properties", null, values);

            if (propertyId != -1 && property.getImagePaths() != null) {
                for (String imagePath : property.getImagePaths()) {
                    ContentValues imageValues = new ContentValues();
                    imageValues.put("property_id", propertyId);
                    imageValues.put("image_path", imagePath);
                    db.insert("property_images", null, imageValues);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return propertyId;
    }

    public List<Property> getAllProperties() {
        List<Property> propertyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM properties", null)) {
            if (cursor.moveToFirst()) {
                do {
                    propertyList.add(cursorToProperty(cursor, db));
                } while (cursor.moveToNext());
            }
        }
        return propertyList;
    }

    private Property cursorToProperty(Cursor cursor, SQLiteDatabase db) {
        int propertyId = cursor.getInt(cursor.getColumnIndexOrThrow("property_id"));
        List<String> imagePaths = getImagesForProperty(propertyId, db);

        return new Property(
                propertyId,
                cursor.getInt(cursor.getColumnIndexOrThrow("admin_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cursor.getString(cursor.getColumnIndexOrThrow("type")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                imagePaths
        );
    }

    private List<String> getImagesForProperty(int propertyId, SQLiteDatabase db) {
        List<String> imagePaths = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT image_path FROM property_images WHERE property_id = ?", new String[]{String.valueOf(propertyId)})) {
            if (cursor.moveToFirst()) {
                do {
                    imagePaths.add(cursor.getString(cursor.getColumnIndexOrThrow("image_path")));
                } while (cursor.moveToNext());
            }
        }
        return imagePaths;
    }

    public List<Property> getPropertiesByAdmin(int adminId) {
        List<Property> propertyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM properties WHERE admin_id = ?", new String[]{String.valueOf(adminId)})) {
            if (cursor.moveToFirst()) {
                do {
                    propertyList.add(cursorToProperty(cursor, db));
                } while (cursor.moveToNext());
            }
        }
        return propertyList;
    }
    public boolean updateProperty(Property property) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int rowsAffected = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("title", property.getTitle());
            values.put("description", property.getDescription());
            values.put("price", property.getPrice());
            values.put("address", property.getAddress());
            values.put("type", property.getType());
            values.put("phone_number", property.getPhoneNumber());

            rowsAffected = db.update("properties", values, "property_id=?", new String[]{String.valueOf(property.getId())});

            db.delete("property_images", "property_id = ?", new String[]{String.valueOf(property.getId())});
            if (property.getImagePaths() != null) {
                for (String imagePath : property.getImagePaths()) {
                    ContentValues imageValues = new ContentValues();
                    imageValues.put("property_id", property.getId());
                    imageValues.put("image_path", imagePath);
                    db.insert("property_images", null, imageValues);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected > 0;
    }

    public Property getPropertyById(int propertyId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Property property = null;
        try (Cursor cursor = db.rawQuery("SELECT * FROM properties WHERE property_id=?", new String[]{String.valueOf(propertyId)})) {
            if (cursor.moveToFirst()) {
                property = cursorToProperty(cursor, db);
            }
        }
        return property;
    }

    public boolean deleteProperty(int propertyId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int rowsAffected = 0;
        try {
            db.delete("property_images", "property_id=?", new String[]{String.valueOf(propertyId)});
            rowsAffected = db.delete("properties", "property_id=?", new String[]{String.valueOf(propertyId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected > 0;
    }

    public List<Property> filterProperties(String type, Double minPrice, Double maxPrice, String location) {
        List<Property> filteredProperties = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgsList = new ArrayList<>();

        if (type != null && !type.isEmpty() && !type.equalsIgnoreCase("All")) {
            selection.append("type = ?");
            selectionArgsList.add(type);
        }

        if (minPrice != null && minPrice > 0) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("price >= ?");
            selectionArgsList.add(String.valueOf(minPrice));
        }

        if (maxPrice != null && maxPrice > 0) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("price <= ?");
            selectionArgsList.add(String.valueOf(maxPrice));
        }

        if (location != null && !location.trim().isEmpty()) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("address LIKE ?");
            selectionArgsList.add("%" + location + "%");
        }

        if (selection.length() == 0) {
            return getAllProperties();
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);
        String selectionStr = selection.toString();

        try (Cursor cursor = db.query("properties", null, selectionStr, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    filteredProperties.add(cursorToProperty(cursor, db));
                } while (cursor.moveToNext());
            }
        }
        return filteredProperties;
    }
}

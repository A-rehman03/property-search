package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import models.Property;

public class FavoriteDao {

    private DatabaseHelper dbHelper;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_USER_FK_ID = "user_id";
    private static final String COLUMN_PROPERTY_FAVORITE_FK_ID = "property_id";

    public FavoriteDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addFavorite(int userId, int propertyId) {
        if (!isFavorite(userId, propertyId)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_FK_ID, userId);
            values.put(COLUMN_PROPERTY_FAVORITE_FK_ID, propertyId);
            db.insert(TABLE_FAVORITES, null, values);
            db.close();
        }
    }

    public void removeFavorite(int userId, int propertyId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_USER_FK_ID + " = ? AND " + COLUMN_PROPERTY_FAVORITE_FK_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(propertyId)};
        db.delete(TABLE_FAVORITES, whereClause, whereArgs);
        db.close();
    }

    public boolean isFavorite(int userId, int propertyId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_USER_FK_ID + " = ? AND " + COLUMN_PROPERTY_FAVORITE_FK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(propertyId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        // Do not close the database here, it might be used by other DAOs
        return exists;
    }

    public List<Property> getFavoriteProperties(int userId) {
        List<Property> favoriteProperties = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        final String MY_QUERY = "SELECT p.* FROM " + PropertyDao.TABLE_PROPERTIES + " p INNER JOIN " +
                TABLE_FAVORITES + " f ON p."
                + PropertyDao.COLUMN_PROPERTY_ID + " = f."
                + COLUMN_PROPERTY_FAVORITE_FK_ID + " WHERE f."
                + COLUMN_USER_FK_ID + " = ?";

        Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Property property = new Property(
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_PROPERTY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_ADMIN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertyDao.COLUMN_PHONE_NUMBER)),
                        new ArrayList<>() // Image paths would need another query, simplified for now
                );
                favoriteProperties.add(property);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteProperties;
    }
}

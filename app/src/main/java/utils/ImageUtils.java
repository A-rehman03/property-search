package utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {

    public static void pickFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, Constants.REQUEST_GALLERY);
    }

    public static Uri openCamera(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Property Image");
        Uri imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, Constants.REQUEST_CAMERA);

        return imageUri;
    }

    // Convert URI to real file path
    public static String getRealPath(Activity activity, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) return null;

        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(colIndex);
        cursor.close();
        return filePath;
    }

    // Save Bitmap to internal storage and return path
    public static String saveBitmap(Activity activity, Bitmap bitmap) {
        File dir = new File(activity.getFilesDir(), "images");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}

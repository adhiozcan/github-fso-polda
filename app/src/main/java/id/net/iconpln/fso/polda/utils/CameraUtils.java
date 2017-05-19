package id.net.iconpln.fso.polda.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Ozcan createNew 25/11/2016.
 */

public class CameraUtils {
    private static final String IMAGE_DIRECTORY_NAME = "fso_polda_kepri";

    public static boolean canOpenCamera(Context ctx) {
        if (!CameraUtils.isDeviceSupportCamera(ctx)) {
            Toast.makeText(ctx, "Device tidak mendukung camera", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!CameraUtils.checkCameraPermission(ctx)) {
            Toast.makeText(ctx, "Aplikasi belum mendapat izin untuk menggunakan kamera", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isDeviceSupportCamera(Context ctx) {
        if (ctx.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkCameraPermission(Context ctx) {
        final String packageName = ctx.getApplicationContext().getPackageName();
        try {
            final PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(android.Manifest.permission.CAMERA)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Camera", "checkCameraPermission: ", e);
        }
        return false;
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public static String getPathFromGallery(Context ctx, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /*
    * returning image
    */
    public static File getOutputMediaFile() {

        // External sdcard location
        File directory = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);

        if (!directory.exists()) {
            directory.mkdir();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(directory.getPath() + File.separator + "QR_" + timeStamp + ".jpg");

        Log.d("Camera", "getOutputMediaFile: " + mediaFile.getAbsolutePath());

        return mediaFile;
    }
}

package id.net.iconpln.fso.polda.utils.camera;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.utils.L;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Ozcan createNew 01/12/2016.
 */

public class PhotoUtils {

    public static class Upload {

        public static MultipartBody.Part prepareImageForUpload(Context ctx, Uri fileUri) {
            System.out.println("Prepare Image For Upload FileUri --> " + fileUri);
            System.out.println("Prepare Image For Upload Path --> " + fileUri.getPath());
            System.out.println("Prepare Image For Upload Name --> " + fileUri.getLastPathSegment());

            File mediaFile           = new File(fileUri.getPath()).getAbsoluteFile();
            File mediaFileCompressed = Compressor.getDefault(ctx).compressToFile(mediaFile);
            return MultipartBody.Part.createFormData(
                    "file",
                    mediaFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), mediaFileCompressed));

        }

        private static String getRealPathFromURI(Context ctx, Uri contentUri) {
            String[]     proj         = {MediaStore.Images.Media.DATA};
            CursorLoader loader       = new CursorLoader(ctx, contentUri, proj, null, null, null);
            Cursor       cursor       = loader.loadInBackground();
            int          column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
    }

    public static class Download {

        public static void getAndSet(Context context, String photoName, ImageView placeHolder) {
            String imageUrl = ServiceUrl.IMAGE + "namafile=" + photoName;
            Picasso.with(context)
                    .load(imageUrl)
                    .resize(150, 150)
                    .centerCrop()
                    .into(placeHolder);
        }
    }

    public static class Common {
        private static BaseAlbumDirFactory mAlbumStorageDirFactory;
        private static String              mCurrentPhotoPath;

        private static final String JPEG_FILE_PREFIX = "FSO_";
        private static final String JPEG_FILE_SUFFIX = ".jpg";

        private static String getAlbumName() {
            return "FSO";
        }

        private static File getAlbumDir() {
            File storageDir = null;

            if (null == mAlbumStorageDirFactory)
                mAlbumStorageDirFactory = new BaseAlbumDirFactory();

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            L.d("failed to create directory");
                            return null;
                        }
                    }
                }

            } else {
                L.d("External storage is not mounted READ/WRITE.");
            }

            return storageDir;
        }

        public static File createImageFile() {
            // Create an image file name
            String timeStamp     = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File   image         = null;
            try {
                image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, getAlbumDir());
            } catch (IOException e) {
                L.e(e, "Failed to get image physical file");
            }
            return image;
        }

        public static File setUpPhotoFile() {
            File f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            if (null == f) return null;
            return f;
        }

        public static void galleryAddPic(AppCompatActivity activity, String photoPath) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File   f               = new File(photoPath);
            Uri    contentUri      = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            activity.sendBroadcast(mediaScanIntent);
        }

        public static void setOptimumPict(String photoPath, ImageView imageView) {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
            imageView.setImageBitmap(bitmap);
        }

        public static void showImage(AppCompatActivity activity, String fileImage) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater            inflater      = activity.getLayoutInflater();

            final View layout = inflater.inflate(R.layout.layout_display_image, (ViewGroup) activity.findViewById(R.id.layout_root));

            final AlertDialog dialog = dialogBuilder.setView(layout).create();
            dialog.show();

            ImageView imageDisplay = (ImageView) layout.findViewById(R.id.image_display);
            Download.getAndSet(activity, fileImage, imageDisplay);
        }
    }
}

package id.net.iconpln.fso.polda.utils.camera;

import android.os.Environment;

import java.io.File;

/**
 * Created by Ozcan on 17/01/2017.
 */

public final class BaseAlbumDirFactory {
    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}

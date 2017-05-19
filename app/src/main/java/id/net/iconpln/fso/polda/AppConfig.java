package id.net.iconpln.fso.polda;

import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.model.UserProfile;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class AppConfig {

    /**
     * Mode Debug allow you to bypass login and directly move to MainActivity.class
     */
    private static final boolean BYPASS_LOGIN = false;

    /**
     * Tis configuration allowing you to bypass splash screen and directly
     * move to LoginActivity.class
     */
    private static final boolean BYPASS_SPLASH = false;

    /**
     * Offline mode allow mocking using local data and prevent all online fetching data.
     */
    private static final boolean MOCKING_MODE = false;

    /**
     * This configuration can be use to upload only files to server instead of data.
     */
    private static final boolean TRY_UPLOAD_PHOTO_MODE = false;

    /**
     * Make update tahapan will always true.
     * The purpose of this config is to allow mocking update tahapan process.
     */
    private static final boolean MAKE_UPDATE_ALWAYS_TRUE = false;

    public static boolean checkByPassLogin() {
        if (BYPASS_LOGIN) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId("6");
            userProfile.setNama("John");
            userProfile.setNamaLengkap("Johny Andreas");
            userProfile.setUnit("Laka Lantas");
            userProfile.setJabatan("Kabag");

            UserSession.saveUser(userProfile);
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkByPassSplash() {
        return BYPASS_SPLASH;
    }

    public static boolean checkMockingMode() {
        return MOCKING_MODE;
    }

    public static boolean checkUploadPhotoOnly() {
        return TRY_UPLOAD_PHOTO_MODE;
    }

    public static boolean checkUpdateTahapanAlwaysTrue() {
        return MAKE_UPDATE_ALWAYS_TRUE;
    }
}

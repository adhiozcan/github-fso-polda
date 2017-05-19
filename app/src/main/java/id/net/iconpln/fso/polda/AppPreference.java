package id.net.iconpln.fso.polda;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Ozcan on 16/01/2017.
 */

public class AppPreference {

    public static void saveAlwaysRemember(boolean option) {
        Hawk.put("app.user.conf.remember", option);
    }

    public static boolean isAlwaysRemember() {
        return Hawk.get("app.user.conf.remember", false);
    }

    public static void saveConfAutoPhotoAlbum(boolean option) {
        Hawk.put("app.user.conf.photoalbum", option);
    }

    public static boolean isAutoPhotoAlbum() {
        return Hawk.get("app.user.conf.photoalbum", false);
    }

    public static void saveConfSyncInterval(int index, int value) {
        Hawk.put("app.system.sync.interval.index", index);
        Hawk.put("app.system.sync.interval.value", value);
    }

    public static int[] getSyncInterval() {
        int[] syncInterval = new int[2];
        syncInterval[0] = Hawk.get("app.system.sync.interval.index", 4);
        syncInterval[1] = Hawk.get("app.system.sync.interval.value", 4);
        return syncInterval;
    }

    public static void saveConfOfflineMode(boolean option) {
        Hawk.put("app.user.conf.offlinemode", option);
    }

    public static boolean isOfflineModeActive() {
        return Hawk.get("app.user.conf.offlinemode", false);
    }
}

package id.net.iconpln.fso.polda.utils;

import android.app.ActivityManager;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Ozcan on 26/01/2017.
 */

public class MemoryUtils {

    public static void doSomethingMemoryIntensive(Context context) {
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory(context);
        L.d("[Memory Info] There are " + memoryInfo.availMem + " available right now");
    }

    private static ActivityManager.MemoryInfo getAvailableMemory(Context context) {
        ActivityManager            activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo      = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
}

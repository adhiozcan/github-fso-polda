package id.net.iconpln.fso.polda.jobscheduler.job;

import android.content.Context;
import android.content.Intent;

import id.net.iconpln.fso.polda.jobscheduler.JobName;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan on 16/02/2017.
 */

public class ServiceManager {

    public static void register(Context context, JobName jobName) {
        switch (jobName) {
            case UPDATE_LAPORAN:
                context.startService(new Intent(context, FetchLaporan.class));
                L.d("[Service] Update Laporan is now starting");
                break;
            case UPDATE_SIGNAL_INDICATOR:
                context.startService(new Intent(context, SignalInfo.class));
                L.d("[Service] Update signal indicator is now starting");
                break;
            default:
                System.out.println("No service available");
                break;
        }
    }

    public static void unregister(Context context, JobName jobName) {
        switch (jobName) {
            case UPDATE_LAPORAN:
                context.stopService(new Intent(context, FetchLaporan.class));
                L.d("[Service] Update laporan is now unregister");
                break;
            case UPDATE_SIGNAL_INDICATOR:
                context.stopService(new Intent(context, SignalInfo.class));
                L.d("[Service] Update signal info  is now unregister");
                break;
            default:
                System.out.println("No service available");
                break;
        }
    }
}

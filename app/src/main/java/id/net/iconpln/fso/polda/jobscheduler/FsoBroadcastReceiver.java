package id.net.iconpln.fso.polda.jobscheduler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.IntentCompat;
import android.widget.Toast;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.ui.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Ozcan on 25/01/2017.
 */

public class FsoBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent openFsoApps = new Intent(context, MainActivity.class);
        openFsoApps.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
        openFsoApps.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        openFsoApps.addFlags(FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(openFsoApps);

        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Toast.makeText(context, "We've got new broadcast reciever in here", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("FSO Polda Kepri")
                .setContentText("Memperbarui informasi laporan telah selesai")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        int                 mNotificationId = 001;
        NotificationManager mNotifyMgr      = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}

package id.net.iconpln.fso.polda.jobscheduler.job;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.otto.Produce;

import id.net.iconpln.fso.polda.BusProvider;
import id.net.iconpln.fso.polda.model.SignalEvent;
import id.net.iconpln.fso.polda.listener.SignalListener;
import id.net.iconpln.fso.polda.utils.ConnectivityUtils;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan on 14/02/2017.
 */

public class SignalInfo extends Service implements ServiceApi {
    private static boolean  isRunning = false;
    private        Handler  handler   = new Handler();
    private        long     time      = 12_000;
    private        Runnable runnable  = task();

    @Override
    public void start() {
        L.d("[start - signal] Status signal info service is " + isRunning + " shall be starting");
        handler.post(runnable);
        isRunning = true;
    }

    @Override
    public void stop() {
        L.d("[stop - signal] Status signal info service is " + isRunning + " shall be stop");
        stopSelf();
        handler.removeCallbacks(runnable);
        ConnectivityUtils.stopDetermineSignalStrength();
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Signal Info --> Enter onStartCommand()");
        if (!isRunning) {
            start();
        } else {
            stop();
            start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    /**
     * This method is where our sync code will be performing a long task.
     */
    @Override
    public void performJob() {
        ConnectivityUtils.determineSignalStrength(new SignalListener() {
            @Override
            public void onReceivedSignalStrength(int strength, int indicatorColor) {
                L.d("[Signal Info] Recieved Signal Strength");
                BusProvider.getInstance().post(produceUpdateSignalStrength(strength, indicatorColor));
            }
        });
    }

    @Override
    public boolean isRunning() {
        return SignalInfo.isRunning;
    }

    @Produce
    private static SignalEvent produceUpdateSignalStrength(int signalStrength, int strengthColorId) {
        return new SignalEvent(signalStrength, strengthColorId);
    }

    @Override
    public Runnable task() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                performJob();
                handler.postDelayed(this, time);
            }
        };
        return runnable;
    }
}

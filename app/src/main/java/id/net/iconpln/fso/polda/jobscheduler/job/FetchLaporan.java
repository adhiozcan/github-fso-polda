package id.net.iconpln.fso.polda.jobscheduler.job;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.otto.Produce;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import id.net.iconpln.fso.polda.AppPreference;
import id.net.iconpln.fso.polda.BusProvider;
import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.model.LaporanEvent;
import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.model.LaporanList;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan on 26/01/2017.
 */

public class FetchLaporan extends Service implements ServiceApi {
    private static boolean  isRunning = false;
    private        Handler  handler   = new Handler();
    private        long     interval  = AppPreference.getSyncInterval()[1] * 1_000;
    private        Runnable runnable  = task();

    @Override
    public void start() {
        L.d("[start] Status service is " + isRunning + " shall be starting");
        handler.post(runnable);
        isRunning = true;
    }

    @Override
    public void stop() {
        L.d("[stop] Status service is " + isRunning + " shall be stopped");
        stopSelf();
        handler.removeCallbacks(runnable);
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Signal Inf0 --> Enter onStartCommand()");
        if (!isRunning) {
            start();
        } else {
            stop();
            start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.d("[Signal Info] Destroying service");
        stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * This method is where our sync code will be performing a long task.
     */
    @Override
    public void performJob() {
        Calendar cal    = Calendar.getInstance(TimeZone.getDefault());
        String   userId = UserSession.getUser().getUserId();
        //String   bulan  = String.valueOf(cal.get(Calendar.MONTH) + 1);
        //String   tahun  = String.valueOf(cal.get(Calendar.YEAR));

        Map params = new HashMap();
        params.put(Param.LIST_LAPORAN_USER_ID, userId);
        params.put(Param.LIST_LAPORAN_BULAN, "");
        params.put(Param.LIST_LAPORAN_TAHUN, "");

        FsoApiClient client = new RequestServer(ServiceUrl.FETCH_DAFTAR_LAPORAN, params);
        client.execute(new FsoApiListener<LaporanList>() {
            @Override
            public void onResponse(LaporanList response) {
                //Broadcast to all subscriber
                BusProvider.getInstance().post(produceUpdateLaporan(response.getListLaporan()));
            }

            @Override
            public void onFailed(String message) {
                L.d(message);
            }
        });

        //MemoryUtils.doSomethingMemoryIntensive(context);
        L.d("Get interval on " + AppPreference.getSyncInterval()[1] + " | " + interval);
    }

    private void updateIntervalFromPreference() {
        if (AppPreference.getSyncInterval()[1] == -1) {
            stop();
        }
        interval = AppPreference.getSyncInterval()[1] * 1_000;
    }

    @Override
    public boolean isRunning() {
        return FetchLaporan.isRunning;
    }

    @Override
    public Runnable task() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateIntervalFromPreference();
                performJob();
                handler.postDelayed(this, interval);
            }
        };
        return runnable;
    }

    @Produce
    public LaporanEvent produceUpdateLaporan(List<Laporan> updateLaporan) {
        return new LaporanEvent(updateLaporan);
    }
}

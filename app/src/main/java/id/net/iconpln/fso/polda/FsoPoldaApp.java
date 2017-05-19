package id.net.iconpln.fso.polda;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

import id.net.iconpln.fso.polda.model.BasicResponse;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiService;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.utils.LaporanSession;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class FsoPoldaApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        /**
         * Initialize Hawk
         */
        Hawk.init(this).build();

        /**
         * Initialize Sesion Laporan
         */
        LaporanSession.getInstance().init();
    }

    public static Context getContext() {
        return context;
    }
}

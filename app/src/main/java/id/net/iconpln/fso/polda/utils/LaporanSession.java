package id.net.iconpln.fso.polda.utils;

import com.orhanobut.hawk.Hawk;

import id.net.iconpln.fso.polda.model.Laporan;

/**
 * Created by Ozcan createNew 01/12/2016.
 */

public class LaporanSession {
    private static LaporanSession instance;

    private LaporanSession() {
    }

    public static LaporanSession getInstance() {
        if (instance == null)
            instance = new LaporanSession();
        return instance;
    }

    public void init() {
        Hawk.put("SESI_STATUS", false);
    }

    public boolean getStatus() {
        return Hawk.get("SESI_STATUS") == null ? false : (boolean) Hawk.get("SESI_STATUS");
    }

    public void createNew() {
        Hawk.put("SESI_STATUS", true);
    }

    public void finish() {
        Hawk.put("SESI_STATUS", false);
    }

    public void captureFormInput(Laporan laporan) {
        Hawk.put("SESI_LAPORAN_DATA_INPUT", laporan);
    }

    public Laporan getLastInput() {
        return Hawk.get("SESI_LAPORAN_DATA_INPUT");
    }

    public void cleanFormInput() {
        Hawk.delete("SESI_LAPORAN_DATA_INPUT");
    }

    public void clearAllCache() {
        Hawk.deleteAll();
    }
}

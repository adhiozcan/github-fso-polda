package id.net.iconpln.fso.polda.model;

import java.util.List;

import id.net.iconpln.fso.polda.model.Laporan;

/**
 * Created by Ozcan on 26/01/2017.
 */

public class LaporanEvent {
    public final List<Laporan> laporanList;

    public LaporanEvent(List<Laporan> laporanList) {
        this.laporanList = laporanList;
    }
}

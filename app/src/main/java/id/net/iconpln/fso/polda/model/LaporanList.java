package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.net.iconpln.fso.polda.model.Laporan;

/**
 * Created by Ozcan on 16/12/2016.
 */

/**
 * Response object containing available reports
 */
public class LaporanList {
    @SerializedName("data")
    private List<Laporan> list_laporan;

    /**
     * The list of reports
     */
    public List<Laporan> getListLaporan() {
        return list_laporan;
    }
}

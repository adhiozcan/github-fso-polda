package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan createNew 30/11/2016.
 */

public class TrackReport {
    private String laporan_id;
    private String nama_laporan;
    @SerializedName("tgl")
    private String tanggal;
    private String keterangan;
    @SerializedName("sts")
    private String status;

    public String getLaporanId() {
        return laporan_id;
    }

    public void setLaporanId(String idLaporan) {
        this.laporan_id = idLaporan;
    }

    public String getNamaLaporan() {
        return nama_laporan;
    }

    public void setNamaLaporan(String namaLaporan) {
        this.nama_laporan = namaLaporan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

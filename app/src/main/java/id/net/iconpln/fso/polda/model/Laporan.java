package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan createNew 23/11/2016.
 */

public class Laporan {
    @SerializedName("idIssue")
    private String issue_id;
    @SerializedName("no_kejadian")
    private String no_kejadian;
    @SerializedName("prioritas")
    private String prioritas;
    @SerializedName("jKejadian")
    private String jenis_kejadian;
    @SerializedName("judul")
    private String judul;
    @SerializedName("pelaku")
    private String pelaku;
    @SerializedName("tglKejadian")
    private String tanggal_kejadian;
    @SerializedName("waktu_kejadian")
    private String waktu_kejadian;
    @SerializedName("lokasi")
    private String lokasi;
    @SerializedName("status")
    private String status;
    @SerializedName("uraian")
    private String uraian;
    @SerializedName("koordinat")
    private String koordinat;
    @SerializedName("koordinat1")
    private String latitude;
    @SerializedName("koordinat2")
    private String longitude;
    @SerializedName("pelapor")
    private String pelapor;
    @SerializedName("bukti_1")
    private String bukti_1;
    @SerializedName("bukti_2")
    private String bukti_2;
    @SerializedName("bukti_3")
    private String bukti_3;
    @SerializedName("bukti_4")
    private String bukti_4;
    @SerializedName("bukti_5")
    private String bukti_5;
    @SerializedName("bukti_6")
    private String bukti_6;

    @Override
    public String toString() {
        return "Content Laporan : \n" +
                "idIssue='" + issue_id + '\'' +
                ", no_kejadian='" + no_kejadian + '\'' +
                ", prioritas='" + prioritas + '\'' +
                ", jenis_kejadian='" + jenis_kejadian + '\'' +
                ", judul='" + judul + '\'' +
                ", pelaku='" + pelaku + '\'' +
                ", tanggal_kejadian='" + tanggal_kejadian + '\'' +
                ", waktu_kejadian='" + waktu_kejadian + '\'' +
                ", lokasi='" + lokasi + '\'' +
                ", status='" + status + '\'' +
                ", uraian='" + uraian + '\'' +
                ", koordinat='" + koordinat + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", pelapor='" + pelapor + '\'' +
                ", bukti_1='" + bukti_1 + '\'' +
                ", bukti_2='" + bukti_2 + '\'' +
                ", bukti_3='" + bukti_3 + '\'' +
                ", bukti_4='" + bukti_4 + '\'' +
                ", bukti_5='" + bukti_5 + '\'' +
                ", bukti_6='" + bukti_6;
    }

    public String getIdIssue() {
        return issue_id;
    }

    public void setIdIssue(String idIssue) {
        this.issue_id = idIssue;
    }

    public String getNoKejadian() {
        return no_kejadian;
    }

    public void setNoKejadian(String no_kejadian) {
        this.no_kejadian = no_kejadian;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

    public String getJenisKejadian() {
        return jenis_kejadian;
    }

    public void setJenisKejadian(String jenis_kejadian) {
        this.jenis_kejadian = jenis_kejadian;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPelaku() {
        return pelaku;
    }

    public void setPelaku(String pelaku) {
        this.pelaku = pelaku;
    }

    public String getTanggalKejadian() {
        return tanggal_kejadian;
    }

    public void setTanggalKejadian(String tanggal_kejadian) {
        this.tanggal_kejadian = tanggal_kejadian;
    }

    public String getWaktuKejadian() {
        return waktu_kejadian;
    }

    public void setWaktuKejadian(String waktu_kejadian) {
        this.waktu_kejadian = waktu_kejadian;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUraian() {
        return uraian;
    }

    public void setUraian(String uraian) {
        this.uraian = uraian;
    }

    public String getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(String koordinat) {
        this.koordinat = koordinat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPelapor() {
        return pelapor;
    }

    public void setPelapor(String pelapor) {
        this.pelapor = pelapor;
    }

    public String getBukti1() {
        return bukti_1;
    }

    public void setBukti1(String bukti_1) {
        this.bukti_1 = bukti_1;
    }

    public String getBukti2() {
        return bukti_2;
    }

    public void setBukti2(String bukti_2) {
        this.bukti_2 = bukti_2;
    }

    public String getBukti3() {
        return bukti_3;
    }

    public void setBukti3(String bukti_3) {
        this.bukti_3 = bukti_3;
    }

    public String getBukti4() {
        return bukti_4;
    }

    public void setBukti4(String bukti_4) {
        this.bukti_4 = bukti_4;
    }

    public String getBukti5() {
        return bukti_5;
    }

    public void setBukti5(String bukti_5) {
        this.bukti_5 = bukti_5;
    }

    public String getBukti6() {
        return bukti_6;
    }

    public void setBukti6(String bukti_6) {
        this.bukti_6 = bukti_6;
    }
}

package id.net.iconpln.fso.polda.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Ozcan on 20/12/2016.
 */

public class Param {
    public static String LOGIN_USERNAME                            = "username";
    public static String LOGIN_PASSWORD                            = "password";
    public static String LIST_LAPORAN_USER_ID                      = "userid";
    public static String LIST_LAPORAN_TAHUN                        = "tahun";
    public static String LIST_LAPORAN_BULAN                        = "bulan";
    public static String LIST_LAPORAN_ISSUE_ID                     = "idissue";
    public static String POST_LAPORAN_PRIORITAS                    = "prioritas";
    public static String POST_LAPORAN_JUDUL                        = "judul";
    public static String POST_LAPORAN_TANGGAL_KEJADIAN             = "tglkejadian";
    public static String POST_LAPORAN_WAKTU_KEJADIAN               = "waktukejadian";
    public static String POST_LAPORAN_LOKASI                       = "lokasi";
    public static String POST_LAPORAN_KOORDINAT                    = "koordinat";
    public static String POST_LAPORAN_PELAKU                       = "pelaku";
    public static String POST_LAPORAN_URAIAN                       = "uraian";
    public static String POST_LAPORAN_PELAPOR                      = "pelapor";
    public static String POST_LAPORAN_IMAGE_LAPORAN_ID             = "laporan_id";
    public static String POST_LAPORAN_IMAGE_PHOTO_SN               = "photo_serial_number";
    public static String POST_LAPORAN_NAMA_PHOTO                   = "nama_photo";
    public static String POST_LAPORAN_FILE_PHOTO                   = "file_photo";
    public static String POST_TAHAP_LAPORAN_ISSUE_ID               = "issue_id";
    public static String POST_TAHAP_LAPORAN_USER_ID                = "user_id";
    public static String POST_TAHAP_LAPORAN_FUNGSI                 = "fungsi";
    public static String POST_TAHAP_LAPORAN_NAMA_USER              = "nama_user";
    public static String POST_TAHAP_LAPORAN_JENIS_KEJADIAN         = "jenis_kejadian";
    public static String POST_TAHAP_LAPORAN_KETERANGAN             = "keterangan";
    public static String POST_TAHAP_LAPORAN_TAHAPAN                = "tahapan";
    public static String POST_TAHAP_LAPORAN_SELESAI_TANGGAL_LIDIK  = "tanggal_lidik";
    public static String POST_TAHAP_LAPORAN_SELESAI_TANGGAL_SIDIK  = "tanggal_sidik";
    public static String POST_TAHAP_LAPORAN_SELESAI_TANGGAL_BERKAS = "tanggal_berkas";

    /**
     * Parameter login
     */
    private String username;
    private String password;

    /**
     * Parameter untuk input laporan baru
     */
    private String userId;
    private String prioritas;
    private String judul;
    private String taggalKejadian;
    private String lokasi;
    private String koordinat;
    private String pelaku;
    private String uraian;
    private String pelapor;

    /**
     * Parameter untuk upload bukti foto
     */
    private RequestBody        laporanId;
    private RequestBody        photoSerialNumb;
    private RequestBody        photoName;
    private MultipartBody.Part photoFile;

    /**
     * Parameter update tahapan
     */
    private String issueId;
    private String fungsi;
    private String user;
    private String keterangan;
    private String updateKeTahapan;
    private String namaRegu;

    /**
     * Parameter update tahapan selesai
     */
    private String tanggalLidik;
    private String tanggalSidik;
    private String tanggalBerkas;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public String getJudul() {
        return judul;
    }

    public String getTaggalKejadian() {
        return taggalKejadian;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getKoordinat() {
        return koordinat;
    }

    public String getPelaku() {
        return pelaku;
    }

    public String getUraian() {
        return uraian;
    }

    public String getPelapor() {
        return pelapor;
    }

    public RequestBody getLaporanId() {
        return laporanId;
    }

    public RequestBody getPhotoSerialNumb() {
        return photoSerialNumb;
    }

    public RequestBody getPhotoName() {
        return photoName;
    }

    public MultipartBody.Part getPhotoFile() {
        return photoFile;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getFungsi() {
        return fungsi;
    }

    public String getUser() {
        return user;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getUpdateKeTahapan() {
        return updateKeTahapan;
    }

    public String getNamaRegu() {
        return namaRegu;
    }

    public String getTanggalLidik() {
        return tanggalLidik;
    }

    public String getTanggalSidik() {
        return tanggalSidik;
    }

    public String getTanggalBerkas() {
        return tanggalBerkas;
    }

    public static class Builder {
        /**
         * Parameter login
         */
        private String username;
        private String password;

        /**
         * Parameter untuk input laporan baru
         */
        private String userId;
        private String prioritas;
        private String judul;
        private String taggalKejadian;
        private String lokasi;
        private String koordinat;
        private String pelaku;
        private String uraian;
        private String pelapor;

        /**
         * Parameter untuk upload bukti foto
         */
        private RequestBody        laporanId;
        private RequestBody        photoSerialNumb;
        private RequestBody        photoName;
        private MultipartBody.Part photoFile;

        /**
         * Parameter update tahapan
         */
        private String issueId;
        private String fungsi;
        private String user;
        private String keterangan;
        private String updateKeTahapan;
        private String namaRegu;

        /**
         * Parameter update tahapan selesai
         */
        private String tanggalLidik;
        private String tanggalSidik;
        private String tanggalBerkas;


    }


}

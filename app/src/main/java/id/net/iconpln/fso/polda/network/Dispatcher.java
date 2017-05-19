package id.net.iconpln.fso.polda.network;

import android.support.annotation.NonNull;

import java.util.Map;

import id.net.iconpln.fso.polda.model.BasicResponse;
import id.net.iconpln.fso.polda.model.DataInputResponse;
import id.net.iconpln.fso.polda.model.LaporanList;
import id.net.iconpln.fso.polda.model.UserProfileResponse;
import id.net.iconpln.fso.polda.model.TahapanResponse;
import id.net.iconpln.fso.polda.model.TrackReportList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Ozcan on 20/12/2016.
 */

class Dispatcher {

    private static Dispatcher sDispatcher;

    /**
     * Create service dispatcher to make request restfull api
     */
    private FsoApiService fsoApiService = RestBuilder.getApiService(FsoApiService.class);

    protected Call<UserProfileResponse> loginUser(@NonNull Map<String, Object> param) {
        return fsoApiService.loginUser(
                String.valueOf(param.get(Param.LOGIN_USERNAME)),
                String.valueOf(param.get(Param.LOGIN_PASSWORD)));
    }

    protected Call<LaporanList> laporanFetchDaftar(@NonNull Map<String, Object> param) {
        return fsoApiService.fetchDaftarLaporan(
                String.valueOf(param.get(Param.LIST_LAPORAN_USER_ID)),
                String.valueOf(param.get(Param.LIST_LAPORAN_BULAN)),
                String.valueOf(param.get(Param.LIST_LAPORAN_TAHUN)));
    }

    protected Call<TrackReportList> laporanFetchTrackReport(@NonNull Map<String, Object> param) {
        return fsoApiService.fetchTrackReportLaporan(String.valueOf(param.get(Param.LIST_LAPORAN_ISSUE_ID)));
    }

    protected Call<DataInputResponse> laporanInputBaru(@NonNull Map<String, Object> param) {
        return fsoApiService.inputLaporan(
                String.valueOf(param.get(Param.POST_LAPORAN_PRIORITAS)),
                String.valueOf(param.get(Param.POST_LAPORAN_JUDUL)),
                String.valueOf(param.get(Param.POST_LAPORAN_TANGGAL_KEJADIAN)),
                String.valueOf(param.get(Param.POST_LAPORAN_WAKTU_KEJADIAN)),
                String.valueOf(param.get(Param.POST_LAPORAN_LOKASI)),
                String.valueOf(param.get(Param.POST_LAPORAN_KOORDINAT)),
                String.valueOf(param.get(Param.POST_LAPORAN_PELAKU)),
                String.valueOf(param.get(Param.POST_LAPORAN_URAIAN)),
                String.valueOf(param.get(Param.POST_LAPORAN_PELAPOR))
        );
    }

    protected Call<BasicResponse> laporanInputFoto(@NonNull Map<String, Object> param) {
        return fsoApiService.uploadFotoKejadian(
                (RequestBody) param.get(Param.POST_LAPORAN_IMAGE_LAPORAN_ID),
                (RequestBody) param.get(Param.POST_LAPORAN_IMAGE_PHOTO_SN),
                (RequestBody) param.get(Param.POST_LAPORAN_NAMA_PHOTO),
                (MultipartBody.Part) param.get(Param.POST_LAPORAN_FILE_PHOTO)
        );
    }

    protected Call<TahapanResponse> laporanUpdateTahapanPerjalanan(@NonNull Map<String, Object> param) {
        return fsoApiService.updateTahapLaporanPerjalanan(
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_ISSUE_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_USER_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_KETERANGAN))
        );
    }

    protected Call<TahapanResponse> laporanUpdateTahapanPenanganan(@NonNull Map<String, Object> param) {
        return fsoApiService.updateTahapLaporanPenanganan(
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_ISSUE_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_USER_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_JENIS_KEJADIAN)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_KETERANGAN))
        );
    }

    protected Call<TahapanResponse> laporanUpdateTahapanPenangananBukti(@NonNull Map<String, Object> param) {
        return fsoApiService.updateTahapLaporanPenangananBukti(
                (RequestBody) param.get(Param.POST_LAPORAN_IMAGE_LAPORAN_ID),
                (RequestBody) param.get(Param.POST_LAPORAN_IMAGE_PHOTO_SN),
                (RequestBody) param.get(Param.POST_LAPORAN_NAMA_PHOTO),
                (MultipartBody.Part) param.get(Param.POST_LAPORAN_FILE_PHOTO)
        );
    }

    protected Call<TahapanResponse> laporanUpdateTahapanSelesai(@NonNull Map<String, Object> param) {
        return fsoApiService.updateTahapLaporanSelesai(
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_ISSUE_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_USER_ID)),
                String.valueOf(param.get(Param.POST_TAHAP_LAPORAN_KETERANGAN))
        );
    }

    /**
     * Make this class singleton
     */
    private Dispatcher() {
    }

    public static Dispatcher getDispatcher() {
        if (sDispatcher == null) sDispatcher = new Dispatcher();
        return sDispatcher;
    }
}
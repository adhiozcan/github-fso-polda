package id.net.iconpln.fso.polda.network;

import id.net.iconpln.fso.polda.model.BasicResponse;
import id.net.iconpln.fso.polda.model.DataInputResponse;
import id.net.iconpln.fso.polda.model.LaporanList;
import id.net.iconpln.fso.polda.model.UserProfileResponse;
import id.net.iconpln.fso.polda.model.TahapanResponse;
import id.net.iconpln.fso.polda.model.TrackReportList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Ozcan createNew 24/11/2016.
 *
 * @author Ozcan
 */

public interface FsoApiService {

    @FormUrlEncoded
    @POST(ServiceUrl.USER_LOGIN)
    Call<UserProfileResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ServiceUrl.FETCH_DAFTAR_LAPORAN)
    Call<LaporanList> fetchDaftarLaporan(
            @Field("userid") String userId,
            @Field("bulan") String bulan,
            @Field("tahun") String tahun);

    @FormUrlEncoded
    @POST(ServiceUrl.FETCH_TRACK_REPORT_LAPORAN)
    Call<TrackReportList> fetchTrackReportLaporan(@Field("idissue") String issueId);

    @FormUrlEncoded
    @POST(ServiceUrl.INPUT_LAPORAN)
    Call<DataInputResponse> inputLaporan(
            @Field("prioritas") String prioritas,
            @Field("judul") String judul,
            @Field("tglkejadian") String tglKejadian,
            @Field("waktukejadian") String waktuKejadian,
            @Field("lokasi") String lokasi,
            @Field("koordinat") String koordinat,
            @Field("pelaku") String pelaku,
            @Field("uraian") String uraian,
            @Field("pelapor") String pelapor
    );

    @Multipart
    @POST(ServiceUrl.INPUT_LAPORAN_BUKTI)
    Call<BasicResponse> uploadFotoKejadian(
            @Part("idissue") RequestBody id,
            @Part("col") RequestBody col,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(ServiceUrl.UPDATE_TAHAP_PERJALANAN)
    Call<TahapanResponse> updateTahapLaporanPerjalanan(
            @Field("idissue") String idIssue,
            @Field("iduser") String idUser,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST(ServiceUrl.UPDATE_TAHAP_PENANGANAN)
    Call<TahapanResponse> updateTahapLaporanPenanganan(
            @Field("idissue") String idIssue,
            @Field("iduser") String idUser,
            @Field("jlaporan") String jenisLaporan,
            @Field("keterangan") String keterangan
    );

    @Multipart
    @POST(ServiceUrl.UPDATE_TAHAP_PENANGANAN_BUKTI)
    Call<TahapanResponse> updateTahapLaporanPenangananBukti(
            @Part("idissue") RequestBody id,
            @Part("col") RequestBody col,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(ServiceUrl.UPDATE_TAHAP_SELESAI)
    Call<TahapanResponse> updateTahapLaporanSelesai(
            @Field("idissue") String idIssue,
            @Field("iduser") String idUser,
            @Field("keterangan") String keterangan
    );
}

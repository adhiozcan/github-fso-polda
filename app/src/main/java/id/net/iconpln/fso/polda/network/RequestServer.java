package id.net.iconpln.fso.polda.network;

import java.util.Map;

import id.net.iconpln.fso.polda.utils.L;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ozcan on 30/01/2017.
 */

public class RequestServer implements FsoApiClient {
    private String              endPoint;
    private Map<String, Object> parameter;

    public RequestServer(String endPoint, Map<String, Object> parameters) {
        this.endPoint = endPoint;
        this.parameter = parameters;
    }

    @Override
    public void execute(final FsoApiListener fsoListener) {
        Call request = defineRequestType();
        request.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    fsoListener.onResponse(response.body());
                    if (response.body() == null)
                        fsoListener.onFailed("Gagal menterjemahkan response");
                } else {
                    fsoListener.onFailed("Gagal memuat response");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                fsoListener.onFailed(t.getLocalizedMessage());
                L.e(t, t.getLocalizedMessage(), call);
            }
        });
    }

    private Call defineRequestType() {
        Dispatcher mDispatcher = Dispatcher.getDispatcher();
        switch (endPoint) {
            case ServiceUrl.USER_LOGIN:
                return mDispatcher.loginUser(parameter);
            case ServiceUrl.FETCH_DAFTAR_LAPORAN:
                return mDispatcher.laporanFetchDaftar(parameter);
            case ServiceUrl.FETCH_TRACK_REPORT_LAPORAN:
                return mDispatcher.laporanFetchTrackReport(parameter);
            case ServiceUrl.INPUT_LAPORAN:
                return mDispatcher.laporanInputBaru(parameter);
            case ServiceUrl.INPUT_LAPORAN_BUKTI:
                return mDispatcher.laporanInputFoto(parameter);
            case ServiceUrl.UPDATE_TAHAP_PERJALANAN:
                return mDispatcher.laporanUpdateTahapanPerjalanan(parameter);
            case ServiceUrl.UPDATE_TAHAP_PENANGANAN:
                return mDispatcher.laporanUpdateTahapanPenanganan(parameter);
            case ServiceUrl.UPDATE_TAHAP_PENANGANAN_BUKTI:
                return mDispatcher.laporanUpdateTahapanPenangananBukti(parameter);
            case ServiceUrl.UPDATE_TAHAP_SELESAI:
                return mDispatcher.laporanUpdateTahapanSelesai(parameter);
            default:
                return null;
        }
    }
}

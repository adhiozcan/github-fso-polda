package id.net.iconpln.fso.polda.network;

/**
 * Created by Ozcan on 20/12/2016.
 */

public class ServiceUrl extends BaseUrl {
    protected static final String RESOURCE_PATH = "/polisiapp/";

    public static final String IMAGE                         = BASE_URL + RESOURCE_PATH + "getimage?";
    public static final String USER_LOGIN                    = RESOURCE_PATH + "login";
    public static final String FETCH_DAFTAR_LAPORAN          = RESOURCE_PATH + "myissue";
    public static final String FETCH_TRACK_REPORT_LAPORAN    = RESOURCE_PATH + "timeline";
    public static final String INPUT_LAPORAN                 = RESOURCE_PATH + "lapor";
    public static final String INPUT_LAPORAN_BUKTI           = RESOURCE_PATH + "uplbuktilapor";
    public static final String UPDATE_TAHAP_PERJALANAN       = RESOURCE_PATH + "perjalanan";
    public static final String UPDATE_TAHAP_PENANGANAN       = RESOURCE_PATH + "penanganan";
    public static final String UPDATE_TAHAP_PENANGANAN_BUKTI = RESOURCE_PATH + "uplbuktipenanganan";
    public static final String UPDATE_TAHAP_SELESAI          = RESOURCE_PATH + "selesai";

}

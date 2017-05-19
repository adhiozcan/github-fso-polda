package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ozcan on 06/01/2017.
 */

public class TrackReportList {
    @SerializedName("data")
    private List<TrackReport> list_track_report;

    public List<TrackReport> getTrackReportList() {
        return list_track_report;
    }
}

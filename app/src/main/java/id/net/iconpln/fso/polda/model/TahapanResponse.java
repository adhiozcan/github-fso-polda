package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ozcan on 28/12/2016.
 */

public class TahapanResponse {
    @SerializedName("data")
    private List<Status> status;

    public String getStatusTahapan() {
        return status.get(0).getIsSuccess();
    }
}

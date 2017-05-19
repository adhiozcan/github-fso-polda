package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 16/12/2016.
 */

public class BasicResponse {
    @SerializedName("data")
    private Status status;

    public String getStatus() {
        return status.getIsSuccess();
    }
}

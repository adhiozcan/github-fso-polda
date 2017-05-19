package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 14/02/2017.
 */

public class Status {
    @SerializedName("success")
    private String isSuccess;

    public String getIsSuccess() {
        return isSuccess;
    }
}

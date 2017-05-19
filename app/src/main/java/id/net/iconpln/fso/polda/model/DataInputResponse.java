package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 16/12/2016.
 */

public class DataInputResponse {
    @SerializedName("data")
    private DataInput input_laporan;

    public DataInput getResponse() {
        return input_laporan;
    }

    public void setResponse(DataInput dataInput) {
        this.input_laporan = dataInput;

    }
}

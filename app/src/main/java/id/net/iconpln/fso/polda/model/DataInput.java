package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 14/02/2017.
 */

public class DataInput {
    @SerializedName("idissue")
    private String issue_id;
    @SerializedName("success")
    private String status;

    public String getIssueId() {
        return issue_id;
    }

    public void setIssueId(String idIssue) {
        this.issue_id = idIssue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

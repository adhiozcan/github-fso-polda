package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 16/12/2016.
 */

public class UserProfileResponse {
    @SerializedName("data")
    private UserProfile user_profile;

    public UserProfile getUserProfile() {
        return user_profile;
    }

    public void setUserProfile(UserProfile user_profile) {
        this.user_profile = user_profile;
    }
}

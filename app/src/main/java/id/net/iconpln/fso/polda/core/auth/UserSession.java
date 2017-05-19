package id.net.iconpln.fso.polda.core.auth;

import com.orhanobut.hawk.Hawk;

import id.net.iconpln.fso.polda.model.UserProfile;

/**
 * Created by Ozcan on 16/01/2017.
 */

/**
 * Provide user session data, the general purpose is
 * to save the most important data used by accross activities.
 */
public class UserSession {
    public static UserProfile getUser() {
        UserProfile userProfileInfo = Hawk.get("app.user.session", new UserProfile());
        return userProfileInfo;
    }

    public static void saveUser(UserProfile userProfile) {
        Hawk.put("app.user.session", userProfile);
    }

    public static void clearSession() {
        Hawk.delete("app.user.session");
    }
}

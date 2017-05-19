package id.net.iconpln.fso.polda.core.auth;

import java.util.Map;

import id.net.iconpln.fso.polda.model.UserProfileResponse;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;

/**
 * Created by Ozcan on 14/02/2017.
 */

public class LoginManager {

    private final Map<String, Object>                 parameter;
    private final FsoApiListener<UserProfileResponse> callbackListener;

    public static class Builder {
        private Map<String, Object>                 parameter;
        private FsoApiListener<UserProfileResponse> callback_listener;


        public Builder withParam(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Builder provideListener(FsoApiListener<UserProfileResponse> callback_listener) {
            this.callback_listener = callback_listener;
            return this;
        }

        public LoginManager build() {
            return new LoginManager(parameter, callback_listener);
        }
    }

    private LoginManager(Map<String, Object> parameter,
                         FsoApiListener<UserProfileResponse> callbackListener) {
        this.parameter = parameter;
        this.callbackListener = callbackListener;
    }

    public void login() {
        FsoApiClient client = new RequestServer(ServiceUrl.USER_LOGIN, parameter);
        client.execute(new FsoApiListener<UserProfileResponse>() {
            @Override
            public void onResponse(UserProfileResponse response) {
                callbackListener.onResponse(response);
                UserSession.saveUser(response.getUserProfile());
            }

            @Override
            public void onFailed(String message) {
                callbackListener.onFailed(message);
            }
        });
    }
}

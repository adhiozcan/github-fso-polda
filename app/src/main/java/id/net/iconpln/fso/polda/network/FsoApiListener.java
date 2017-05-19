package id.net.iconpln.fso.polda.network;

/**
 * Created by Ozcan on 20/12/2016.
 */

public interface FsoApiListener<T> {
    void onResponse(T response);

    void onFailed(String message);
}
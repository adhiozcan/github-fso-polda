package id.net.iconpln.fso.polda.network;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by Ozcan createNew 24/11/2016.
 */

public interface FsoApiClient<T> {
    void execute(@NonNull final FsoApiListener<T> fsoApiListener);
}

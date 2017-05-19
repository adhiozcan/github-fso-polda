package id.net.iconpln.fso.polda.listener;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public interface MapsCompleteListener {
    void onComplete(LatLng latLng, String locationAddress);
}

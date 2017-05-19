package id.net.iconpln.fso.polda.utils;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ozcan on 10/01/2017.
 */

public class AddressUtils {
    /*public static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                             CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("Google Place", res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));
    }*/

    public static String extractLocationInfo(Geocoder geocoder, LatLng latLng) {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String        prefix        = ", ";
        StringBuilder locationFound = new StringBuilder();

        if (addresses.get(0) == null) return "Unknown Location";

        Address address     = addresses.get(0);
        String  lineAddress = address.getAddressLine(0);
        String  city        = addresses.get(0).getLocality();
        String  state       = addresses.get(0).getAdminArea();
        String  country     = addresses.get(0).getCountryName();
        String  knownName   = addresses.get(0).getFeatureName();

        if (lineAddress != null) locationFound.append(lineAddress + prefix);
        if (city != null) locationFound.append(city + prefix);
        if (state != null) locationFound.append(state + prefix);
        if (country != null) locationFound.append(country + prefix);
        if (knownName != null) locationFound.append(knownName);

        L.d(address + ", " + city + ", " + state + ", " + country + ", " + knownName);
        String formattedAddress = locationFound.toString();
        return formattedAddress;
    }
}

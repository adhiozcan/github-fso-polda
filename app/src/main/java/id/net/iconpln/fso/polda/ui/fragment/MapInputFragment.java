package id.net.iconpln.fso.polda.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.listener.MapsCompleteListener;
import id.net.iconpln.fso.polda.utils.AddressUtils;
import id.net.iconpln.fso.polda.utils.L;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by Ozcan createNew 25/11/2016.
 */

public class MapInputFragment extends DialogFragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener {

    //Provide boundary box for Batam Island
    private static final LatLng       latLng1             = new LatLng(0.618595, 104.296492);
    private static final LatLng       latLng2             = new LatLng(1.103281, 103.796258);
    private static final LatLngBounds BOUNDS_BATAM_ISLAND = new LatLngBounds(latLng1, latLng2);

    public static final  int MY_PERMISSIONS_REQUEST_LOCATION  = 99;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private MapsCompleteListener mMapsCompleteListener;
    private GoogleMap            mMap;
    private AutoCompleteTextView mAutocompleteView;

    private TextView       mPlaceDetailsText;
    private TextView       mPlaceDetailsAttribution;
    private PulsatorLayout mPulsatorLayout;
    private View           btnLocationApproved;
    private View           btnLocationSearch;

    // Provide several Google Maps component
    GoogleApiClient mGoogleApiClient;
    Geocoder        geocoder;

    public static MapInputFragment getInstance(Bundle data, MapsCompleteListener mapCompleteListener) {
        MapInputFragment fragment = new MapInputFragment();
        fragment.setArguments(data);
        fragment.mMapsCompleteListener = mapCompleteListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_input, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        //Check if Google Play Service available or not
        if (!isGooglePlayServiceAvailable()) {
            L.d("Can't using google");
            Toast.makeText(getActivity(),
                    "Perangkat Anda tidak mendukung penggunaan map",
                    Toast.LENGTH_SHORT)
                    .show();
            dismiss();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        btnLocationApproved = view.findViewById(R.id.location_approved);
        //btnLocationSearch = view.findViewById(R.id.location_search);
        mPlaceDetailsText = (TextView) view.findViewById(R.id.place_details);
        mPlaceDetailsAttribution = (TextView) view.findViewById(R.id.place_attribution);
        mPulsatorLayout = (PulsatorLayout) view.findViewById(R.id.pulsator);
        mPulsatorLayout.start();
        view.findViewById(R.id.location_approved).setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void checkLastLatLngFromIntent() {
        if (getArguments() != null) {
            double latitude      = Double.parseDouble(getArguments().getString("Latitude"));
            double longitude     = Double.parseDouble(getArguments().getString("Longitude"));
            LatLng latLng        = new LatLng(latitude, longitude);
            String locationFound = AddressUtils.extractLocationInfo(geocoder, latLng);
            updateUIWhenLocationFound(locationFound, latLng, true);
            L.d("Receiving confirmation about location on " + latitude + ", " + longitude);
        }
    }

    private boolean isGooglePlayServiceAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int                   result    = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }
        return true;
    }


    private void openPlaceAutoComplete() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (
                GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (
                GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setOnMyLocationButtonClickListener(this);

        checkLastLatLngFromIntent();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(getActivity(), this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Map connection suppended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Google API", "ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends createNew this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        mPulsatorLayout.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment            fragment = (getFragmentManager().findFragmentById(R.id.google_map));
        FragmentTransaction ft       = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    private void updateUIWhenLocationFound(String locationAddress, LatLng latLng, boolean needZoomIn) {
        btnLocationApproved.setVisibility(View.VISIBLE);
        mAutocompleteView.setText(locationAddress);
        mAutocompleteView.setFocusable(false);
        mPulsatorLayout.stop();

        //If none latlng found, then stop in here
        if (mMap == null | latLng == null) return;

        //Place current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Set TKP");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.draggable(true);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (needZoomIn) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(8));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mPulsatorLayout.stop();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.setTitle("Set TKP");
        marker.showInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.setTitle("Set TKP");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        double dragLat      = dragPosition.latitude;
        double dragLong     = dragPosition.longitude;

        L.d("New marker position at " + dragLat + ", " + dragLong);

        //Update obtained location to view
        LatLng latLng        = new LatLng(dragLat, dragLong);
        String locationFound = AddressUtils.extractLocationInfo(geocoder, latLng);
        updateUIWhenLocationFound(locationFound, latLng, false);
        mMapsCompleteListener.onComplete(latLng, locationFound);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        L.d("New marker position at " + latLng.latitude + ", " + latLng.longitude);

        String locationFound = AddressUtils.extractLocationInfo(geocoder, latLng);
        updateUIWhenLocationFound(locationFound, latLng, false);
        mMapsCompleteListener.onComplete(latLng, locationFound);
    }
}

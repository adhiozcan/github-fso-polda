/*
package id.net.iconpln.fso.polda.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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
import id.net.iconpln.fso.polda.adapter.PlaceAutoCompleteAdapter;
import id.net.iconpln.fso.polda.listener.MapsCompleteListener;
import id.net.iconpln.fso.polda.utils.AddressUtils;
import id.net.iconpln.fso.polda.utils.L;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

*/
/**
 * Created by Ozcan createNew 25/11/2016.
 *//*


public class MapInputFragmentBackup extends DialogFragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMyLocationButtonClickListener {

    //Provide boundary box for Batam Island
    private static final LatLng       latLng1             = new LatLng(0.618595, 104.296492);
    private static final LatLng       latLng2             = new LatLng(1.103281, 103.796258);
    private static final LatLngBounds BOUNDS_BATAM_ISLAND = new LatLngBounds(latLng1, latLng2);

    public static final  int MY_PERMISSIONS_REQUEST_LOCATION  = 99;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private MapsCompleteListener     mMapsCompleteListener;
    private GoogleMap                mMap;
    private PlaceAutoCompleteAdapter mAdapter;
    private AutoCompleteTextView     mAutocompleteView;

    private TextView       mPlaceDetailsText;
    private TextView       mPlaceDetailsAttribution;
    private PulsatorLayout mPulsatorLayout;
    private View           btnLocationApproved;
    private View           btnLocationSearch;

    // Provide several Google Maps component
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Geocoder        geocoder;

    public static MapInputFragmentBackup getInstance(Bundle data, MapsCompleteListener mapCompleteListener) {
        MapInputFragmentBackup fragment = new MapInputFragmentBackup();
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

        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.place_picker);
        mAutocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startLocationUpdates(mAdapter.getItem(i));
            }
        });
        mAutocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAutocompleteView.setText("");
            }
        });

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

    protected void startLocationUpdates(AutocompletePrediction prediction) {

        */
/**
         Retrieve the place ID of the selected item from the Adapter.
         The adapter stores each Place suggestion in a AutocompletePrediction from which we
         read the place ID and title.
         *//*

        final AutocompletePrediction item        = prediction;
        final String                 placeId     = item.getPlaceId();
        final CharSequence           primaryText = item.getPrimaryText(null);

        L.d("Autocomplete item selected : " + primaryText);

        */
/**
         Issue a request to the Places Geo Data API to retrieve a Place object with
         additional details about the place.
         *//*

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);

        */
/**
         * Callback for results from a Places Geo Data API query that shows the first place
         * result in the details view createNew screen.
         *//*

        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    Log.e("Google Place", "Place query did not complete. Error: " + places.getStatus().toString());
                    places.release();
                    return;
                }
                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
                }

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                L.d("Get the place object from the buffer --> " + place.toString());

                LatLng location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                L.d("Place details received : " + place.getName());
                mMapsCompleteListener.onComplete(location, place.getName().toString());

                // Format details of the place for display and show it in a TextView.
                String locationFound = place.getName() + ", " + place.getAddress();
                updateUIWhenLocationFound(locationFound, location, true);

                places.release();
            }
        });
        L.d("Called getPlaceById to get Place details for " + placeId);
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

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API
        // that cover on specific location.
        mAdapter = new PlaceAutoCompleteAdapter(getActivity(),
                mGoogleApiClient,
                BOUNDS_BATAM_ISLAND,
                null);

        mAutocompleteView.setAdapter(mAdapter);

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
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(getActivity(), this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapInputFragmentBackup.this);
        }
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
    public void onLocationChanged(Location location) {
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        //Update obtained location to view
        LatLng latLng        = new LatLng(location.getLatitude(), location.getLongitude());
        String locationFound = AddressUtils.extractLocationInfo(geocoder, latLng);
        updateUIWhenLocationFound(locationFound, latLng, true);
        mMapsCompleteListener.onComplete(latLng, locationFound);
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
*/

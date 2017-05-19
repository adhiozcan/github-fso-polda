package id.net.iconpln.fso.polda.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.net.iconpln.fso.polda.R;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class MapInfoFragment extends DialogFragment implements OnMapReadyCallback {
    private static MapInfoFragment mFragment = new MapInfoFragment();
    private GoogleMap mMap;
    private String mLocationName;
    private LatLng mLatLng;

    public static MapInfoFragment create(String locationName, LatLng latLng) {
        mFragment.mLocationName = locationName;
        mFragment.mLatLng = latLng;
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_info, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        view.findViewById(R.id.closed_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f));
        mMap.addMarker(new MarkerOptions().position(mLatLng).title(mLocationName)).setDraggable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.google_map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}

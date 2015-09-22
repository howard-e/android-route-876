package com.route876.fragments.homefragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.route876.R;
import com.route876.fragments.PlanRouteFragment;

/**
 * Created by Howard on 9/12/2015.
 */
public class MyRouteFragment extends Fragment {
    private final static int DEFAULT_ZOOM_LEVEL = 10;
    private final static LatLng UWI_Mona = new LatLng(18.004427300000000000, -76.746225700000020000); // TODO: Remove when no longer in need of dummy location

//    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//    private static final String API_KEY = "AIzaSyCi6DW6GAo_6ZfIJird5p0CHxsNH8hNgLA";
//    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
//    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
//    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    private SupportMapFragment supportMapFragment;
    private PlanRouteFragment planRouteFragment;
    private GoogleMap mMap;
    private double _latitude;
    private double _longitude;
    private double _radius;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_route, container, false);
        LinearLayout planRouteButton = (LinearLayout) rootView.findViewById(R.id.plan_route_click);
        LinearLayout nearMeButton = (LinearLayout) rootView.findViewById(R.id.near_me_click);

        planRouteFragment = new PlanRouteFragment();
        planRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.y_enter, R.anim.y_exit, R.anim.y_pop_enter, R.anim.y_pop_exit);
                fragmentTransaction.replace(R.id.container, planRouteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Use Google Places API to display bus stops close to user current location based on a certain radius
                getCurrentLocation();
            }
        });
        return rootView;
    }

    public void initMap() {
        mMap.addMarker(new MarkerOptions().position(UWI_Mona).title("UWI Mona")); // TODO: Remove when no longer in need of dummy location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UWI_Mona, DEFAULT_ZOOM_LEVEL)); // TODO: Remove when no longer in need of dummy location
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getMyLocation();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = supportMapFragment.getMap();
            initMap();
        }
    }

    private void checkLocationService() {
        final LocationManager locationManager = (LocationManager) getActivity().
                getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageLocationDisabled();
            Toast.makeText(getActivity(), "Location Unavailable", Toast.LENGTH_SHORT).show();
        } else if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            buildAlertMessageLocationDisabled();
            Toast.makeText(getActivity(), "Location Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildAlertMessageLocationDisabled() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Location Settings Disabled");
        builder.setIcon(R.mipmap.ic_logo_black);
        builder.setMessage("Your Location Settings seem to be disabled, do you want to enable " +
                " it for the best user experience?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings
                                .ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void getCurrentLocation() {
        Location myLocation = mMap.getMyLocation();
        if (myLocation != null) {
            double myLatitude = myLocation.getLatitude();
            double myLongitude = myLocation.getLongitude();

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLatitude, myLongitude), DEFAULT_ZOOM_LEVEL));
        } else {
            checkLocationService();
        }
    }
}

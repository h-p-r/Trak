package com.hpr.ac.dev.trak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        CustomSupportMapFragment.OnMapFragmentReadyListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public MapFragment() {
    }

    // Create the Handler
    private Handler handler = new Handler();

    // Define the code block to be executed
    private Runnable runnable = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            // Insert custom code here
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to Mountain View
                                        .zoom(17)                   // Sets the zoom
                                        .bearing(90)                // Sets the orientation of the camera to east
                                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }
                    });
            // Repeat every 2 seconds
            handler.postDelayed(runnable, 2000);
        }
    };

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // create SupportMapFragment, and listen for onMapfragmentReady callback
        mMapFragment = CustomSupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.flMapContainer, mMapFragment).commit();
        return v;
    }

    // This method gets called when CustomSupportMapFragment has been initialized and is ready for
    // map initialization
    public void onMapFragmentReady() {
        mMapFragment.getMapAsync(this);  // async create GoogleMap, calls onMapReady when ready
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // button in upper right of map that zooms to current location when pressed
        map.getUiSettings().setMyLocationButtonEnabled(true);
        enableMyLocation();

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            createLocationRequest();


            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            handler.post(runnable);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior till occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
}
package com.hpr.ac.dev.trak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hpr.ac.dev.trak.ui.CustomImageViewforMarker;

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
    private DatabaseReference mDatabase;
    Marker marker = null;

    private static final int RED_MARKER = 3;
    private static final int GREEN_MARKER = 1;
    private static final int ORANGE_MARKER = 2;


    private SupportMapFragment mMapFragment;
    private GoogleMap mMap = null;

    private FusedLocationProviderClient mFusedLocationClient;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public MapFragment() {
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(17)
                                        .bearing(90)
                                        .tilt(60)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }
                    });
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
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NodeData note = noteDataSnapshot.getValue(NodeData.class);
                    System.out.println(note.busnumber);
                    System.out.println(note.lat);
                    System.out.println(note.lon);

                    int cond = note.cond + 1;

                    if (mMap != null) {
                        if (marker != null)
                            marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(note.lat, note.lon)).
                                icon(BitmapDescriptorFactory.fromBitmap(
                                        createCustomMarker(getContext(), cond, note.busnumber))));
                        marker.setTitle("Licence No: " + note.licence_no + "\nDestination: " + note.busdest +
                                "\nBus Type:" + note.type + " " + note.floor_type);

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NodeData note = noteDataSnapshot.getValue(NodeData.class);
                    System.out.println(note.busnumber);
                    System.out.println(note.lat);
                    System.out.println(note.lon);

                    int cond = note.cond + 1;

                    if (mMap != null) {
                        if (marker != null)
                            marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(note.lat, note.lon)).
                                icon(BitmapDescriptorFactory.fromBitmap(
                                        createCustomMarker(getContext(), cond, note.busnumber))));
                        marker.setTitle("Licence No: " + note.licence_no + "\nDestination: " + note.busdest +
                                "\nBus Type:" + note.type + " " + note.floor_type);

                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMapFragment = CustomSupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.flMapContainer, mMapFragment).commit();
        return v;
    }

    public void onMapFragmentReady() {
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("tag", "done");
        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

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
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            createLocationRequest();
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            handler.post(runnable);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
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
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    public void adddata() {
        String uid = mDatabase.push().getKey();
        NodeData mNode = new NodeData("adad", "asad", "gfg", "ghj", "rtrt", 123.4, 345.6, 2);
        mDatabase.child(uid).setValue(mNode, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                } else {
                    Log.e("FormActivity", error.toString());
                }
            }
        });
    }

    public static Bitmap createCustomMarker(Context context, int color, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CustomImageViewforMarker markerImage = marker.findViewById(R.id.custom_marker);
        markerImage.setImageResourcewithColor(color);
        TextView txt_name = marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
}
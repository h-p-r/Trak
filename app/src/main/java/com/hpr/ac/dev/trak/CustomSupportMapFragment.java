package com.hpr.ac.dev.trak;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class CustomSupportMapFragment extends SupportMapFragment {

    public static interface OnMapFragmentReadyListener {
        void onMapFragmentReady();
    }

    public CustomSupportMapFragment() {
        super();
    }

    public static CustomSupportMapFragment newInstance() {
        return new CustomSupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);

        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnMapFragmentReadyListener) {
            ((OnMapFragmentReadyListener) fragment).onMapFragmentReady();
        }
        return v;
    }
}
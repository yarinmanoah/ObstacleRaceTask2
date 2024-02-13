package com.example.obstacleracetask2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GPSTracker tracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tracker = new GPSTracker(getContext());
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        if(mapFragment == null){
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map , mapFragment).commit();
        }
        mapFragment.getMapAsync( this);
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        tracker.removeLocationObservers(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        tracker.removeLocationObservers(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        tracker.getLocationMutableLiveData().observe(this, location -> {
            if(location==null || mMap == null) return ;
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(current)
                    .title("  "));
            System.out.println("Reached in");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,14.0f));
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if(tracker.getLocationCurrent() != null) {
            Location location = tracker.getLocationCurrent();
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(current)
                    .title("  "));
            System.out.println("Reached in");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));
        }
    }
    public GoogleMap getmMap() {
        return mMap;
    }
}

package com.placego.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.placego.R;
import com.placego.util.PersonLocation;

import java.util.List;

/**
 * Created by juanes on 1/12/15.
 */
public class HomeMap extends Fragment implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback

{

    private ClusterManager<PersonLocation> mClusterManager;
    //private ParseUser currentUser;

    private GoogleMap mMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mClusterManager = new ClusterManager<PersonLocation>(getActivity(), mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        addMarkersToMap();
    }


    private void addMarkersToMap() {
        ParseQuery<PersonLocation> query = PersonLocation.getQuery();
        query.include("fk_user");
        query.whereEqualTo("activo", true);
        query.findInBackground(new FindCallback<PersonLocation>() {
            @Override
            public void done(List<PersonLocation> objects, ParseException e) {
                if (e == null) {
                    getUsers(objects);
                }
            }
        });
    }

    private void getUsers(List<PersonLocation> list) {
        LatLng latlng;
        for (PersonLocation person : list) {
            latlng = new LatLng(person.getLocation().getLatitude(), person.getLocation().getLongitude());
            person.setPosition(latlng);
            mClusterManager.addItem(person);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-22.824070, 134.815227), 3));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }


}

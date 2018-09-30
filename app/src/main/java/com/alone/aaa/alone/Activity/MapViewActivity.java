package com.alone.aaa.alone.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alone.aaa.alone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double getLat = getIntent().getDoubleExtra("lat", 1);
        Double getLng = getIntent().getDoubleExtra("lng", 1);
        String getName = getIntent().getStringExtra("name");
        String getAddr = getIntent().getStringExtra("addr");


        MarkerOptions makerMarkerOptions = new MarkerOptions();
        LatLng seoul = new LatLng(getLat, getLng);
        makerMarkerOptions.position(seoul);
        makerMarkerOptions.title(getName).snippet(getAddr);
        mMap.addMarker(makerMarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }
}
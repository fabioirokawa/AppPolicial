package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalMaps  extends AppCompatActivity implements OnMapReadyCallback {

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("Latitude", 0);
        longitude = intent.getDoubleExtra("Longitude", 0);

        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
		assert mapFragment != null;
		mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Adicionando um alfinete na latitude e longitude informadas
        LatLng alfinete = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(alfinete).title("Suspeito"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alfinete, 13));
    }
}

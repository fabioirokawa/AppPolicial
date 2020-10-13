package com.example.apppolicial;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ObtemLocal extends AppCompatActivity {

    FusedLocationProviderClient client;

    public Double[] getLocalizacao() {

        client = LocationServices.getFusedLocationProviderClient(this);
        final Double[] latlong = {0.0, 0.0};

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null)
                    {
                        //latlong[0] = location.getLatitude();
                        //latlong[1] = location.getLongitude();
                    }
                }
            });
        }
        return latlong;

    }
}

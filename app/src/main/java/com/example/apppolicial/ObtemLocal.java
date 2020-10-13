package com.example.apppolicial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ObtemLocal extends AppCompatActivity {

    FusedLocationProviderClient client;

    public Double[] getLocalizacao(Context context) {

        client = LocationServices.getFusedLocationProviderClient(context);
        final Double[] latlong = {0.0, 0.0};

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null)
                    {
                        latlong[0] = location.getLatitude();
                        latlong[1] = location.getLongitude();
                        Log.i("COORDINATES",latlong[0].toString()+"/"+latlong[1].toString());
                    }
                }
            });
        }
        return latlong;

    }
}

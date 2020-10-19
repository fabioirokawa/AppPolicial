package com.example.apppolicial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ObtemLocal extends AppCompatActivity {

    FusedLocationProviderClient client;
    private double longitude;
	private double latitude;

    public Double[] getLocalizacao(Context context) {

        client = LocationServices.getFusedLocationProviderClient(context);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(location == null){
				Log.d("TAG", "Failed to retrieve location");
				return new Double[]{0.0, 0.0};
			}
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.d("TAG", String.valueOf(latitude)+","+ longitude);

		}
        return new Double[]{latitude, longitude};

    }
}

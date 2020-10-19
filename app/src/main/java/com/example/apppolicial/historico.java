package com.example.apppolicial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class historico extends AppCompatActivity implements android.location.LocationListener {
	int i = 0;
	static String anteriorTemp = "";
	static Suspeito[] suspeitos = new Suspeito[10];
	static ArrayList<Suspeito> histDadosList = new ArrayList<>();
	private double longitude;
	private double latitude;
	private LocationManager lm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico);
		getLocalizacao();
	}

	public void getLocalizacao() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		Log.d("LOCATION", "Requesting locations");
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			String[] locationPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
			ActivityCompat.requestPermissions(this, locationPermissions, 1);
			return;
		}
		if(!isGPSEnabled){
			Toast.makeText(this, "Por favor ative os serviços de localização",Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}else {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
	}
	@Override
	public void onLocationChanged(@NonNull Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("LOCATION", String.valueOf(latitude) + "," + longitude);
		lm.removeUpdates(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "Obrigado", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Precisamos dessas permissoes!", Toast.LENGTH_SHORT).show();
			ActivityCompat.requestPermissions(this,permissions , 1);
			getLocalizacao();
		}
	}


	protected void onStart() {
		super.onStart();
		int j = 0;
		String nomeSuspeito = "";
		setContentView(R.layout.activity_historico);

		Bundle b = getIntent().getExtras();

		nomeSuspeito = b.getString("a");
		Bitmap rostoSuspeito = getIntent().getParcelableExtra("b");

		if(b.getString("a") == null)
		{
			nomeSuspeito = "";
		}
		if (!(nomeSuspeito.equals(anteriorTemp)))
		{
			suspeitos[i] = new Suspeito(nomeSuspeito, "Crime cometido","Alto", rostoSuspeito, 0.0, 0.0);
			histDadosList.add(suspeitos[j]);
			j++;
			anteriorTemp = nomeSuspeito;
			HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
			ListView mListView = findViewById(R.id.histList);
			mListView.setAdapter(adapter);
		}
		else
		{
			HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
			ListView mListView = findViewById(R.id.histList);
			mListView.setAdapter(adapter);
		}
		i++;
	}

	public void goToMaps(double latitude, double longitude){
		Intent intent = new Intent(this, LocalMaps.class);
		intent.putExtra("Latitude", latitude);
		intent.putExtra("Longitude", longitude);
		startActivity(intent);
	}


}

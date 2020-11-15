package com.example.apppolicial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class historico extends AppCompatActivity {
	int i = 0;
	static String anteriorTemp = "";
	static Suspeito[] suspeitos = new Suspeito[10];
	static ArrayList<Suspeito> histDadosList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico);

	}

	protected void onStart() {
		super.onStart();
		int j = 0;
		String nomeSuspeito = "";
		setContentView(R.layout.activity_historico);

		Bundle b = getIntent().getExtras();

		nomeSuspeito = b.getString("name");
		String idadeDoSuspeito = b.getString("age");
		String nivelPerigoDoSuspeito = b.getString("dangerLevel");
		String[] listaDeCrimesDoSuspeito = b.getStringArray("crimes");
		String probabilidadeDoSuspeito = b.getString("probability");
		double[] locations = b.getDoubleArray("locations");
		Bitmap rostoSuspeito = getIntent().getParcelableExtra("face");


		if(b.getString("name") == null)
		{
			nomeSuspeito = "";
		}

		if (!(nomeSuspeito.equals(anteriorTemp)))
		{
			suspeitos[i] = new Suspeito(nomeSuspeito,Integer.parseInt(idadeDoSuspeito), listaDeCrimesDoSuspeito, nivelPerigoDoSuspeito, rostoSuspeito, locations[0], locations[1]);
			histDadosList.add(suspeitos[j]);
			j++;
			anteriorTemp = nomeSuspeito;
			HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
			ListView mListView = findViewById(R.id.histList);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		else
		{
			HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
			ListView mListView = findViewById(R.id.histList);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
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

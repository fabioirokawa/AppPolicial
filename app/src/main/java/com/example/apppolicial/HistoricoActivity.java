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

import com.example.apppolicial.room.CopEyeDatabase;
import com.example.apppolicial.room.Detection;
import com.example.apppolicial.room.DetectionDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
	int i = 0;
	private Suspeito[] suspeitos;
	private HistDadosListAdapter adapter;
	static ArrayList<Suspeito> histDadosList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico);
		List<Detection> detections = CopEyeDatabase.getInstance(this).dataDao().getAll();

		suspeitos = new Suspeito[detections.size()];
		for( Detection d : detections ) {
			String[] arr = new String[d.crimes.size()];
			suspeitos[i] = new Suspeito(d.nome,d.idade,d.crimes.toArray(arr),d.nivel_perigo,d.imagem_dataset,d.latitude,d.longitude);
			i++;
		}


		histDadosList.addAll(Arrays.asList(suspeitos));
		adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
		ListView mListView = findViewById(R.id.histList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();


		Button clearHistoryButton = (Button) findViewById(R.id.clear_history);
		clearHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CopEyeDatabase.getInstance(HistoricoActivity.this).dataDao().deleteAll();
				histDadosList.removeAll(Arrays.asList(suspeitos));
				adapter.notifyDataSetChanged();
			}
		});

	}


	public void goToMaps(double latitude, double longitude){
		Intent intent = new Intent(this, LocalMaps.class);
		intent.putExtra("Latitude", latitude);
		intent.putExtra("Longitude", longitude);
		startActivity(intent);
	}


}

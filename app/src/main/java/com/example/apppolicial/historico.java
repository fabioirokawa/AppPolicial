package com.example.apppolicial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

	public void goToMaps(Double latitude, Double longitude){
		Intent intent = new Intent(this, LocalMaps.class);
		intent.putExtra("Latitude", latitude);
		intent.putExtra("Longitude", longitude);
		startActivity(intent);
	}
}

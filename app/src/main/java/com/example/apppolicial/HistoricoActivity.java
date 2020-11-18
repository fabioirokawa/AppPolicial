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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.apppolicial.room.CopEyeDatabase;
import com.example.apppolicial.room.Detection;
import com.example.apppolicial.room.DetectionDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
	private Suspeito[] suspeitos;
	private HistDadosListAdapter adapter;
	static ArrayList<Suspeito> histDadosList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico);

		adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
		ListView mListView = findViewById(R.id.histList);
		mListView.setAdapter(adapter);

		updateList();


		final SwipeRefreshLayout refresh = findViewById(R.id.swipeRefressHistory);
		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				updateList();
				refresh.setRefreshing(false);
			}
		});

		Button clearHistoryButton = (Button) findViewById(R.id.clear_history);
		clearHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CopEyeDatabase.getInstance(HistoricoActivity.this).dataDao().deleteAll();

				updateList();
			}
		});
	}



	void updateList(){
		List<Detection> detections = CopEyeDatabase.getInstance(HistoricoActivity.this).dataDao().getAll();

		histDadosList.clear();
		suspeitos = new Suspeito[detections.size()];
		int i = 0;
		for( Detection d : detections ) {
			String[] arr = new String[d.crimes.size()];
			suspeitos[i] = new Suspeito(d.nome,d.idade,d.crimes.toArray(arr),d.nivel_perigo,d.imagem_dataset,d.latitude,d.longitude);
			suspeitos[i].setSuspeito_id(d.id);
			i++;
		}

		histDadosList.addAll(Arrays.asList(suspeitos));
		adapter.notifyDataSetChanged();
	}


}

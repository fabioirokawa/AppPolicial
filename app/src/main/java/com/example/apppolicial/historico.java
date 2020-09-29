package com.example.apppolicial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class historico extends AppCompatActivity {
    private Button btn_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        btn_maps = (Button) findViewById(R.id.maps);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMaps();
            }
        });
    }

    public void goToMaps(){
        ListView mListView = (ListView) findViewById(R.id.histList);

        hist_dados carlos = new hist_dados("Carlos","Assalto", R.drawable.sampa_background);
        hist_dados andre = new hist_dados("Andre","Roubo", R.drawable.sampa_background);
        hist_dados reginaldo = new hist_dados("Reginaldo","Não usou máscara", R.drawable.sampa_background);

        ArrayList<hist_dados> histDadosList = new ArrayList<>();
        histDadosList.add(carlos);
        histDadosList.add(andre);
        histDadosList.add(reginaldo);

        HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
        mListView.setAdapter(adapter);

        Intent intent = new Intent(this, LocalMaps.class);
        intent.putExtra("Latitude", -22.835132);
        intent.putExtra("Longitude", -47.050473);
        startActivity(intent);
    }
}
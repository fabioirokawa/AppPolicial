package com.example.apppolicial;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class historico extends AppCompatActivity {
    private Button btn_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        ListView mListView = (ListView) findViewById(R.id.histList);

        Suspeito carlos = new Suspeito("Carlos","Assalto", BitmapFactory.decodeResource(getBaseContext().getResources(),R.drawable.sampa_background));
        Suspeito andre = new Suspeito("Andre","Roubo", BitmapFactory.decodeResource(getBaseContext().getResources(),R.drawable.sampa_background));
        Suspeito reginaldo = new Suspeito("Reginaldo","Não usou máscara", BitmapFactory.decodeResource(getBaseContext().getResources(),R.drawable.sampa_background));

        ArrayList<Suspeito> histDadosList = new ArrayList<>();
        histDadosList.add(carlos);
        histDadosList.add(andre);
        histDadosList.add(reginaldo);

        HistDadosListAdapter adapter = new HistDadosListAdapter(this, R.layout.layout_historico, histDadosList);
        mListView.setAdapter(adapter);

        btn_maps = (Button) findViewById(R.id.maps);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMaps();
            }
        });
    }

    public void goToMaps(){

        Intent intent = new Intent(this, LocalMaps.class);
        intent.putExtra("Latitude", -22.835132);
        intent.putExtra("Longitude", -47.050473);
        startActivity(intent);
    }
}

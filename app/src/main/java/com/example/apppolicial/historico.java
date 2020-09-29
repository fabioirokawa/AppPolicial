package com.example.apppolicial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class historico extends AppCompatActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


		Button btn_maps = findViewById(R.id.maps);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMaps();
            }
        });
    }

    public void goToMaps(){
        ListView mListView = (ListView) findViewById(R.id.histList);

        Suspect carlos = new Suspect("Carlos","Assalto", R.drawable.sampa_background);
        Suspect andre = new Suspect("Andre","Roubo", R.drawable.sampa_background);
        Suspect reginaldo = new Suspect("Reginaldo","Não usou máscara", R.drawable.sampa_background);

        ArrayList<Suspect> histDadosList = new ArrayList<>();
        histDadosList.add(carlos);
        histDadosList.add(andre);
        histDadosList.add(reginaldo);

        SuspectListAdapter adapter = new SuspectListAdapter(this, R.layout.layout_historico, histDadosList);
        mListView.setAdapter(adapter);

        Intent intent = new Intent(this, LocalMaps.class);
        intent.putExtra("Latitude", -22.835132);
        intent.putExtra("Longitude", -47.050473);
        startActivity(intent);
    }
}

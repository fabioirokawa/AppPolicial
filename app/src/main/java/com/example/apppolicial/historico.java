package com.example.apppolicial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
        Intent intent = new Intent(this, LocalMaps.class);
        intent.putExtra("Latitude", -22.835132);
        intent.putExtra("Longitude", -47.050473);
        startActivity(intent);
    }
}
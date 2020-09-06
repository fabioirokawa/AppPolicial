package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button camera;
    private Button historico;
    private Button historico2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (Button) findViewById(R.id.bCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        historico = (Button) findViewById(R.id.bHistorico);
        historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHist();
            }
        });
    }

    public void goToCamera(){
        Intent intent = new Intent(this, camera2.class);
        startActivity(intent);
    }

    public void goToHist(){
        Intent intent = new Intent(this, camera.class);
        startActivity(intent);
    }
}
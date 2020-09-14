package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final ImageView imageView = findViewById(R.id.perfilView);

        imageView.setImageResource(R.drawable.seloco);

        Button b1 = findViewById(R.id.test1);
        Button b2 = findViewById(R.id.test2);
        Button b3 = findViewById(R.id.test3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackgroundResource(R.drawable.perfil_ok);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackgroundResource(R.drawable.perfil_atencao);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackgroundResource(R.drawable.perfil_perigoso);
            }
        });


    }

}
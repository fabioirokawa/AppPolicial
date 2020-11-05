package com.example.apppolicial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class perfil extends AppCompatActivity {

    String PNome, PPeri;
    Bitmap PFoto;
	String[] PCrime;

    private ImageView ivFoto;
    private TextView tvPNome, tvPCrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();


        PNome = intent.getStringExtra("pNome");
        PCrime = intent.getStringArrayExtra("pCrime");
        PPeri = intent.getStringExtra("pPerigo");
        PFoto = intent.getParcelableExtra("pFoto");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ivFoto = findViewById(R.id.perfilView);
        tvPNome = findViewById(R.id.tSuspeitoNome);
        tvPCrime = findViewById(R.id.tSuspeitoCrime);

        ivFoto.setImageBitmap(PFoto);
        tvPNome.setText(PNome);
        tvPCrime.setText(PCrime[0]);

		switch (PPeri) {
			case "Alto":
				ivFoto.setBackgroundResource(R.drawable.perfil_perigoso);
				break;
			case "Medio":
				ivFoto.setBackgroundResource(R.drawable.perfil_atencao);
				break;
			case "Baixo":
				ivFoto.setBackgroundResource(R.drawable.perfil_ok);
				break;
		}
    }
}

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();


		String PNome = intent.getStringExtra("name");
		String[] PCrime = intent.getStringArrayExtra("crimes");
		String PPeri = intent.getStringExtra("dangerLevel");
		Bitmap PFoto = intent.getParcelableExtra("face");
		String idade = intent.getStringExtra("age");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageView ivFoto = findViewById(R.id.perfilView);
		TextView tvPNome = findViewById(R.id.nomePerfil);
		TextView tvPCrime = findViewById(R.id.crimesPerfil);
        TextView tvPIdade = findViewById(R.id.idadePerfil);
		TextView tvPPeri = findViewById(R.id.nivelPerigoPerfil);
		String crimesList = "";
        for (String c : PCrime){
        	if (c != null) crimesList += c + ", ";
        }
        crimesList += "!";
		crimesList = crimesList.replace(", !","");
		crimesList = crimesList.replace(", , ",", ");
		//crimesList = crimesList.trim();

        ivFoto.setImageBitmap(PFoto);
        tvPNome.setText(PNome);
        tvPCrime.setText(crimesList);
        tvPIdade.setText(idade);
        tvPPeri.setText(PPeri);


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

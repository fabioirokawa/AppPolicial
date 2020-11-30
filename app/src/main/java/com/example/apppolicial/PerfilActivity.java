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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.apppolicial.room.*;

import java.util.List;

public class PerfilActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

		int suspeito_id = intent.getIntExtra("id",-99);
		if (suspeito_id == -99){
			this.finish();
		}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


		List<Detection> detections = CopEyeDatabase.getInstance(this).dataDao().findById(suspeito_id);
		if(detections.size() > 1){
			Toast.makeText(this, "ERRO MAIS DE UM SUSPEITO ID", Toast.LENGTH_LONG).show();
		}

		String PNome;
		String idade;
		String PPeri;
		Bitmap PFoto;
		Detection d = detections.get(0);

		String[] PCrime = new String[d.crimes.size()];
		PCrime = d.crimes.toArray(PCrime);

		PNome = d.nome;
		idade = d.idade;
		PPeri = d.nivel_perigo;
		PFoto = d.imagem_dataset;

        ImageView ivFoto = findViewById(R.id.perfilView);
		TextView tvPNome = findViewById(R.id.nomePerfil);
		TextView tvPCrime = findViewById(R.id.crimesPerfil);
        TextView tvPIdade = findViewById(R.id.idadePerfil);
		TextView tvPPeri = findViewById(R.id.nivelPerigoPerfil);

		StringBuilder crimesList = new StringBuilder();

		for (String c : PCrime){
			if (c != null) crimesList.append(c).append(", ");
		}
        crimesList.append("!");
		crimesList = new StringBuilder(crimesList.toString().replace(", !", ""));
		crimesList = new StringBuilder(crimesList.toString().replace(", , ", "!!"));
		crimesList = new StringBuilder(crimesList.toString().replace(" , ", ""));
		crimesList = new StringBuilder(crimesList.toString().replace("!!", ", "));
		//crimesList = crimesList.trim();

        ivFoto.setImageBitmap(PFoto);
        tvPNome.setText(PNome);
        tvPCrime.setText(crimesList.toString());
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

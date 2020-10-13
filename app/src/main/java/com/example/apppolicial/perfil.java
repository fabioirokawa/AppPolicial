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

    String PNome, PCrime, PPeri;
    Bitmap PFoto;

    private ImageView ivFoto;
    private TextView tvPNome, tvPCrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        PNome = (String) intent.getStringExtra("pNome");
        PCrime = (String) intent.getStringExtra("pCrime");
        PPeri = (String) intent.getStringExtra("pPerigo");
        PFoto = (Bitmap) intent.getParcelableExtra("pFoto");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ivFoto = (ImageView) findViewById(R.id.perfilView);
        tvPNome = (TextView) findViewById(R.id.tSuspeitoNome);
        tvPCrime = (TextView) findViewById(R.id.tSuspeitoCrime);

        Log.d("aaaaaaaaaaaaaaaaaaaaaa",PNome);
        Log.d("aaaaaaaaaaaaaaaaaaaaaa",PCrime);
        Log.d("aaaaaaaaaaaaaaaaaaaaaa",PPeri);

        ivFoto.setImageBitmap(PFoto);
        tvPNome.setText(PNome);
        tvPCrime.setText(PCrime);

        if (PPeri.equals("Alto")){
            ivFoto.setBackgroundResource(R.drawable.perfil_perigoso);
        }
        else if (PPeri.equals("Medio")){
            ivFoto.setBackgroundResource(R.drawable.perfil_atencao);
        }
        else if (PPeri.equals("Baixo")){
            ivFoto.setBackgroundResource(R.drawable.perfil_ok);
        }
    }
}
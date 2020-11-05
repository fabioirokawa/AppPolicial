package com.example.apppolicial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistDadosListAdapter extends ArrayAdapter<Suspeito> {

    private Context nContext;
    int nResource;
    Bitmap imageSus;
    String nome;
    String[] crimes;
    String peri;
    int idade;

    Double[] latlong = new Double[2];

    public HistDadosListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Suspeito> objects) {
        super(context, resource, objects);
        nContext = context;
        nResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        nome = getItem(position).getNome();

        crimes = getItem(position).getCrimes();

        imageSus = getItem(position).getFotoDoSuspeito();

        peri = getItem(position).getPericulosidade();
        idade = getItem(position).getIdadeDoSuspeito();
        Double localizacao[] = getItem(position).getLocalizacao();

        Suspeito DadosHist = new Suspeito(nome,idade , crimes, peri, imageSus, localizacao[0], localizacao[1]);

        LayoutInflater inflater = LayoutInflater.from(nContext);
        convertView = inflater.inflate(nResource, parent, false);

        Button bMapa = convertView.findViewById(R.id.bMapsHist);
        TextView tvNome = convertView.findViewById(R.id.textNome);
        TextView tvCrime = convertView.findViewById(R.id.textCrime);
        ImageButton ivSus = convertView.findViewById(R.id.imageSus);



        bMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nContext, LocalMaps.class);
                intent.putExtra("Latitude", latlong[0]);
                intent.putExtra("Longitude", latlong[1]);
                nContext.startActivity(intent);
            }
        });

        tvNome.setText(nome);
        tvCrime.setText(crimes[0]);
        ivSus.setImageBitmap(imageSus);

        ivSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nContext, perfil.class);
                intent.putExtra("pNome", nome);
                intent.putExtra("pCrime", crimes);
                intent.putExtra("pPerigo", peri);
				intent.putExtra("pFoto", imageSus);
                nContext.startActivity(intent);
            }
        });

        return convertView;
    }


}

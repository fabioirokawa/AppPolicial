package com.example.apppolicial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistDadosListAdapter extends ArrayAdapter<Suspeito> {

    private Context nContext;
    int nResource;
    ArrayList<Suspeito> nObjects;
    Bitmap imageSus;
    String nome;
    String[] crimes;
    String peri;
    int idade;
    String idadeS;
    ListView listview;

    Double[] latlong = new Double[2];

    public HistDadosListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Suspeito> objects) {
        super(context, resource, objects);
        nContext = context;
        nResource = resource;
        nObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        nome = getItem(position).getNome();

        crimes = getItem(position).getCrimes();

        imageSus = getItem(position).getFotoDoSuspeito();

        peri = getItem(position).getPericulosidade();

        idade = getItem(position).getIdadeDoSuspeito();

        final Double localizacao[] = getItem(position).getLocalizacao();

        Suspeito DadosHist = new Suspeito(nome,idade , crimes, peri, imageSus, localizacao[0], localizacao[1]);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(nContext);
            convertView = inflater.inflate(nResource, parent, false);
        }


        Button bMapa = convertView.findViewById(R.id.bMapsHist);
        TextView tvNome = convertView.findViewById(R.id.layTextNome);
        TextView tvCrime = convertView.findViewById(R.id.layTextCrime);
        ImageButton ivSus = convertView.findViewById(R.id.imageSus);
        ListView listView = convertView.findViewById(R.id.histList);


        bMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nContext, LocalMaps.class);
                intent.putExtra("Latitude", localizacao[0]);
                intent.putExtra("Longitude", localizacao[1]);
                nContext.startActivity(intent);
            }
        });

        tvNome.setText(nome);
        tvCrime.setText(crimes[position]);
        ivSus.setImageBitmap(imageSus);
        Log.d("1111111111111111" , nome + "//" + crimes + "//" + idade);

        ivSus.setTag(position);
        ivSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                Intent intent = new Intent(nContext, perfil.class);
                int position = (Integer) view.getTag();
                Bundle b = new Bundle();
                Log.d("9999999999999999" , nome + "//" + crimes + "//" + idade);
                String nome = getItem(position).getNome();
                String[] crimes = getItem(position).getCrimes();
                Bitmap imageSus = getItem(position).getFotoDoSuspeito();
                String peri = getItem(position).getPericulosidade();
                int idade  = getItem(position).getIdadeDoSuspeito();
                b.putString("name", nome);
                b.putStringArray("crimes", crimes);
                b.putString("dangerLevel", peri);
                b.putString("age", Integer.toString(idade));
				intent.putExtra("face", imageSus);
				intent.putExtras(b);
                nContext.startActivity(intent);
            }
        });

        return convertView;
    }


}

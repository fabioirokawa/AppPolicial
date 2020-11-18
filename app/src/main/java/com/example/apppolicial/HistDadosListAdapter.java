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
    String idade;
    String crimesS = "";
    // --Commented out by Inspection (17/11/20 13:29):ListView listview;

    // --Commented out by Inspection (17/11/20 13:29):Double[] latlong = new Double[2];

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

        final Double[] localizacao = getItem(position).getLocalizacao();

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


        bMapa.setTag(position);
        bMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nContext, LocalMaps.class);
                intent.putExtra("Latitude", localizacao[0]);
                intent.putExtra("Longitude", localizacao[1]);

                int pos = (Integer) view.getTag();
                intent.putExtra("id",getItem(pos).getSuspeito_id());
                nContext.startActivity(intent);
            }
        });
		crimesS="";

        tvNome.setText(nome);
        for (String c : crimes){
            if (c != null) crimesS += c + ", ";
        }
        crimesS += "!";
        crimesS = crimesS.replace(", !","");
        crimesS = crimesS.replace(", , ","!!");
        crimesS = crimesS.replace(" , ","");
        crimesS = crimesS.replace("!!",", ");

        tvCrime.setText(crimesS);
        ivSus.setImageBitmap(imageSus);

        ivSus.setTag(position);
        ivSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                Intent intent = new Intent(nContext, PerfilActivity.class);
                int position = (Integer) view.getTag();
                Bundle b = new Bundle();
                int suspeito_id = getItem(position).getSuspeito_id();
                b.putInt("id",suspeito_id);
                intent.putExtras(b);
                nContext.startActivity(intent);
            }
        });

        return convertView;
    }


}

package com.example.apppolicial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.internal.location.zzn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class HistDadosListAdapter extends ArrayAdapter<Suspeito> {

    historico hhh = new historico();
    private Context nContext;
    int nResource;

    Double[] latlong = new Double[2];

    public HistDadosListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Suspeito> objects) {
        super(context, resource, objects);
        nContext = context;
        nResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nome = getItem(position).getNome();
        String crime = getItem(position).getCrime();
        Bitmap imageSus = getItem(position).getFotoDoSuspeito();
        String peri = getItem(position).getPericulosidade();
        Double localizacao[] = getItem(position).getLocalizacao();

        Suspeito DadosHist = new Suspeito(nome, crime, peri, imageSus, localizacao[0], localizacao[1]);

        LayoutInflater inflater = LayoutInflater.from(nContext);
        convertView = inflater.inflate(nResource, parent, false);

        Button bMapa = (Button) convertView.findViewById(R.id.bMapsHist);
        TextView tvNome = (TextView) convertView.findViewById(R.id.textNome);
        TextView tvCrime = (TextView) convertView.findViewById(R.id.textCrime);
        ImageView ivSus = (ImageView) convertView.findViewById(R.id.imageSus);



        bMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ObtemLocal local = new ObtemLocal();
                latlong = local.getLocalizacao();

                Intent intent = new Intent(nContext, LocalMaps.class);
                intent.putExtra("Latitude", latlong[0]);
                intent.putExtra("Longitude", latlong[1]);
                nContext.startActivity(intent);
            }
        });
        tvNome.setText(nome);
        tvCrime.setText(crime);
        ivSus.setImageBitmap(imageSus);

        return convertView;
    }


}

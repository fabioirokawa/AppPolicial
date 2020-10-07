package com.example.apppolicial;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistDadosListAdapter extends ArrayAdapter<Suspeito> {

    private Context nContext;
    int nResource;

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

        Suspeito DadosHist = new Suspeito(nome, crime, imageSus);

        LayoutInflater inflater = LayoutInflater.from(nContext);
        convertView = inflater.inflate(nResource, parent, false);

        TextView tvNome = (TextView) convertView.findViewById(R.id.textNome);
        TextView tvCrime = (TextView) convertView.findViewById(R.id.textCrime);
        ImageView ivSus = (ImageView) convertView.findViewById(R.id.imageSus);

        tvNome.setText(nome);
        tvCrime.setText(crime);
        ivSus.setImageBitmap(imageSus);

        return convertView;
    }
}

package com.example.apppolicial;

import android.graphics.Bitmap;



public class Suspeito {

    private Bitmap fotoDoSuspeito;
    private String nome= "";
    private String crime= "";
    private String periculosidade="";
    private Double latitude;
    private Double longitude;


    public Suspeito(String nome, String crime,String periculosidade, Bitmap imgSus, Double latitude, Double longitude) {
        this.nome = nome;
        this.crime = crime;
        this.fotoDoSuspeito = imgSus;
        this.periculosidade = periculosidade;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome(){
        return nome;
    }

    public String getCrime(){
        return crime;
    }

    public Bitmap getFotoDoSuspeito(){
        return fotoDoSuspeito;
    }

    public String getPericulosidade() {
		return periculosidade;
	}

	public Double[] getLocalizacao() { Double[] loc = {latitude, longitude}; return loc;}


}

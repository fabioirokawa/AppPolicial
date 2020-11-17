package com.example.apppolicial;

import android.graphics.Bitmap;


public class Suspeito {

    private Bitmap fotoDoSuspeito;
    private String nome= "";
    private String[] crimes;
    private int idadeDoSuspeito;
    private String periculosidade="";
    private double latitude;
    private double longitude;


    public Suspeito(String nome, int idade,String[] crimes,String periculosidade, Bitmap imgSus, double latitude, double longitude) {
        this.nome = nome;
        this.crimes = crimes;
        this.idadeDoSuspeito = idade;
        this.fotoDoSuspeito = imgSus;
        this.periculosidade = periculosidade;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome(){
        return nome;
    }

    public String[] getCrimes(){
        return crimes;
    }

    public Bitmap getFotoDoSuspeito(){
        return fotoDoSuspeito;
    }

    public String getPericulosidade() {
		return periculosidade;
	}

	public Double[] getLocalizacao() {
		return new Double[]{latitude, longitude};}

	public int getIdadeDoSuspeito() {
		return idadeDoSuspeito;
	}
}

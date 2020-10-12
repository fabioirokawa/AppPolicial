package com.example.apppolicial;

import android.graphics.Bitmap;



public class Suspeito {

    private Bitmap fotoDoSuspeito;
    private String nome= "";
    private String crime= "";
    private String periculosidade="";


    public Suspeito(String nome, String crime,String periculosidade, Bitmap imgSus) {
        this.nome = nome;
        this.crime = crime;
        this.fotoDoSuspeito = imgSus;
        this.periculosidade = periculosidade;
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


}

package com.example.apppolicial;

import android.graphics.Bitmap;

public class Suspeito {
    private Bitmap fotoDoSuspeito;
    private String nome;
    private String crime;

    public Suspeito(String nome, String crime, Bitmap imgSus) {
        this.nome = nome;
        this.crime = crime;
        this.fotoDoSuspeito = imgSus;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getCrime(){
        return crime;
    }
    public void setCrime(String crime) {
        this.crime = crime;
    }
    public Bitmap getFotoDoSuspeito(){
        return fotoDoSuspeito;
    }
    public void setFotoDoSuspeito(Bitmap fotoDoSuspeito) {
        this.fotoDoSuspeito = fotoDoSuspeito;
    }
}

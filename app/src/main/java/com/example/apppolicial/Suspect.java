package com.example.apppolicial;

public class Suspect {
    private int imgSus;
    private String nome;
    private String crime;

    public Suspect(String nome, String crime, int imgSus) {
        this.nome = nome;
        this.crime = crime;
        this.imgSus = imgSus;
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
    public int getImgSus(){
        return imgSus;
    }
    public void setImgSus(int imgSus) {
        this.imgSus = imgSus;
    }
}

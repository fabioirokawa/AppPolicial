package com.example.apppolicial;

public class hist_dados {
    private int imgSus;
    private String nome;
    private String crime;

    public hist_dados(String nome, String crime, int imgSus) {
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

package com.example.apppolicial;

public class hist_dados {
    private String nome;
    private String crime;

    public hist_dados(String nome, String crime) {
        this.nome = nome;
        this.crime = crime;
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
}

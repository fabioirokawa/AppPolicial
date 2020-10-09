package com.example.apppolicial;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Suspeito {

	private static List<String> coresDaPele = Arrays.asList("Pardo","Branco","Negro","Amarelo");



    private Bitmap fotoDoSuspeito;
    private String nome= "";
    private String crime= "";
    private double periculosidade=0;
	private String corDaPele = "";


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

    public double getPericulosidade() {
		return periculosidade;
	}

	public void setPericulosidade(double periculosidade) {
		this.periculosidade = periculosidade;
	}

	public String getCorDaPele() {
		return corDaPele;
	}

	public void setCorDaPele(String corDaPele) {
		this.corDaPele = corDaPele;
	}
}

package com.example.apppolicial.room;


import android.graphics.Bitmap;

import androidx.room.*;

import java.io.File;
import java.util.ArrayList;


@Entity(tableName="detections")
public class Detection {

	public Detection(String nome,
					 String idade,
					 String probabilidade,
					 String time,
					 String nivel_perigo,
					 Bitmap imagem_frame,
					 Bitmap imagem_crop,
					 Bitmap imagem_dataset,
					 ArrayList<String> crimes,
					 double latitude,
					 double longitude){

		this.nome=nome;
		this.probabilidade=probabilidade;
		this.time=time;
		this.nivel_perigo=nivel_perigo;
		this.imagem_frame=imagem_frame;
		this.imagem_crop=imagem_crop;
		this.imagem_dataset=imagem_dataset;
		this.crimes=crimes;
		this.idade=idade;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@PrimaryKey(autoGenerate = true)
	public int id;

	public String nome;

	public String idade;

	public String probabilidade;

	public String time;

	public String nivel_perigo;

	public Bitmap imagem_frame;

	public Bitmap imagem_crop;

	public Bitmap imagem_dataset;

	public ArrayList<String> crimes;

	public double latitude;

	public double longitude;

}

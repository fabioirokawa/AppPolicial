package com.example.apppolicial.room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
	@TypeConverter
	public static String BitMapToString(Bitmap bitmap) {

		if (bitmap == null){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
		byte [] b=baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	@TypeConverter
	public static Bitmap StringToBitmap(String encodedString){
		try{
			byte[] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
		}catch(Exception e){
			e.getMessage();
			return null;
		}
	}

	@TypeConverter
	public static ArrayList<String> fromString(String value) {
		Type listType = new TypeToken<ArrayList<String>>() {}.getType();
		return new Gson().fromJson(value, listType);
	}

	@TypeConverter
	public static String fromArrayList(ArrayList<String> list) {
		Gson gson = new Gson();
		return gson.toJson(list);
	}





}

package com.example.apppolicial.room;

import android.content.Context;

import androidx.room.*;

@Database(entities = {Detection.class},version = 1,exportSchema = false)

@TypeConverters({Converters.class})
public abstract class CopEyeDatabase extends RoomDatabase {
	public abstract DetectionDao dataDao();


	public static final String DATABASE_NAME = "CopEye";

	public static CopEyeDatabase instance;

	public static CopEyeDatabase getInstance(Context context) {
		if(instance == null){
			instance = Room.databaseBuilder(context,CopEyeDatabase.class,DATABASE_NAME).allowMainThreadQueries().build();
		}
		return instance;
	}


}

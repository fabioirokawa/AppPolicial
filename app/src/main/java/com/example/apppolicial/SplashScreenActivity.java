package com.example.apppolicial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

		View decorView = getWindow().getDecorView();

		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);


		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startApp();
			}
		},1000);

    }

	private void startApp(){
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
		finish();

	}

}

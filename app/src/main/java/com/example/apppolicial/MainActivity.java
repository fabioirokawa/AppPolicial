package com.example.apppolicial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apppolicial.room.*;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements android.location.LocationListener{
	private final int REQUES_MULTIPLE_PERMISSIONS= 1;
	private final int ASK_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

	private DetectionDao database;
	private Bitmap hRostoSuspeito;
	private String nomeDoSuspeito;
	private String idadeDoSuspeito;
	private String nivelPerigoDoSuspeito;
	private String[] listaDeCrimesDoSuspeito;
	private String horaDeteccaoSuspeito;
	private String probabilidadeDoSuspeito;
	private Button buttonProfile;
	private double longitude;
	private double latitude;
	private LocationManager lm;
	private final String[] permissions = new String[] {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1){
			if(!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))){
				new AlertDialog.Builder(this)
						.setTitle("Por favor ative os serviços de localização")
						.setPositiveButton("Ativar localização",new AlertDialog.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
								startActivityForResult(i,1);
							}
						}).setIcon(R.drawable.ic_location_on_24px)
				.setCancelable(false)
				.show();
			}else {
				getLocalizacao();
				Thread myThread = new Thread(new MyServer(this));
				myThread.start();//Inicia thread de conexão via socket
			}
		}
		if (requestCode == 2){
			ArrayList<String> deniedPermissions = new ArrayList<String>();
			for (String p : permissions) {
				if (ActivityCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_DENIED) {
					deniedPermissions.add(p);
				}
			}
			if(deniedPermissions.size() > 0){
				ActivityCompat.requestPermissions(this, permissions,REQUES_MULTIPLE_PERMISSIONS);
			}else {
				Toast.makeText(this, R.string.thanks_permissions, Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {


			if (requestCode == REQUES_MULTIPLE_PERMISSIONS) {

				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, R.string.thanks_permissions, Toast.LENGTH_LONG).show();
				} else if(grantResults.length  != 0) {
					new AlertDialog.Builder(this)
							.setTitle(R.string.alert_permissions_title)
							.setCancelable(false)
							.setMessage(R.string.alert_permissions_description)
							.setPositiveButton(R.string.see_permissions,new AlertDialog.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
									i.addCategory(Intent.CATEGORY_DEFAULT);
									i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
									i.setData(Uri.parse("package:"+ getPackageName()));
									startActivityForResult(i,2);
								}
							})
							.setNegativeButton("OK",new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									exitApplication();
								}
							})
							.show();
				}
			}
	}

	private void exitApplication() {finish();}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<String> deniedPermissions = new ArrayList<String>();
		for (String p : permissions) {
			if (ActivityCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_DENIED) {
				deniedPermissions.add(p);
			}
		}
		if(deniedPermissions.size() > 0){
			ActivityCompat.requestPermissions(this, permissions,REQUES_MULTIPLE_PERMISSIONS);
		}
		buttonProfile = findViewById(R.id.botaoPerfil);
		buttonProfile.setVisibility(View.INVISIBLE);

		Button buttonHistory = findViewById(R.id.botaoHistorico);
		Button buttonForm = findViewById(R.id.formula);



		buttonForm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goToForm();
			}
		});
		buttonProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToPerf();
			}
		});
		buttonHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToHist();
			}
		});

		lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if(!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))){
			new AlertDialog.Builder(this)
					.setTitle("Por favor ative os serviços de localização")
					.setPositiveButton("Ativar localização",new AlertDialog.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							startActivityForResult(i,1);
						}
					}).setIcon(R.drawable.ic_location_on_24px)
					.setCancelable(false)
					.show();
		}else{
			getLocalizacao();
			Thread myThread = new Thread(new MyServer(this));
			myThread.start();//Inicia thread de conexão via socket

		}
	}


	public void goToForm(){
		Intent intent = new Intent (this, FormularioActivity.class);
		startActivity(intent);
	}

	public void goToHist(){
		Intent intent = new Intent (this, HistoricoActivity.class);
		startActivity(intent);
	}

	public void goToPerf(){
		Intent intent = new Intent (this, PerfilActivity.class);
		Bundle b = new Bundle();
		b.putString("name", nomeDoSuspeito);
		intent.putExtra("face", hRostoSuspeito);
		b.putString("probability", probabilidadeDoSuspeito);
		b.putString("time", horaDeteccaoSuspeito);
		b.putString("age", idadeDoSuspeito);
		b.putString("dangerLevel", nivelPerigoDoSuspeito);
		b.putStringArray("crimes", listaDeCrimesDoSuspeito);
		intent.putExtras(b);
		startActivity(intent);
	}

    class MyServer implements  Runnable{
        ServerSocket ss;
        Socket mySocket;
        Handler handler = new Handler(Looper.getMainLooper());//Usado para acessar a thread principal
        Context context;
        byte[] aByte = new byte[1024];
        int bytesRead;
        InputStream is;
		File pathFrame;
		File pathFaceCrop;
		File pathMatchDataset;
		String[] mensagemSeparada;
		boolean error=false;
		NotifyAlert notifyAlert = new NotifyAlert();
		FileOutputStream fos;
		BufferedOutputStream bos;



		MyServer(Context c){
            context = c;
        }
        @Override
        public void run() {
            try {
				ss = new ServerSocket(9700);
				handler.post(new Runnable() {
					@Override
					public void run() {
						Snackbar.make(findViewById(R.id.botaoHistorico),"Aguardando conexão",Snackbar.LENGTH_LONG).show();

					}
				});//Acessa thread principal para exibir mensagem flutuante
			}catch (BindException ex){
				error =true;
			}
            catch (IOException ex){
				ex.printStackTrace();
				error=true;
			}

			database = CopEyeDatabase.getInstance(context).dataDao();


            Bitmap btmp = null;

            Detection ddd = new Detection("Fulano","22","15%","hora","Alto",
					btmp,btmp,btmp,new ArrayList<>(Arrays.asList("crime","crime2")),0,0);
			database.insertAll(ddd);

			while (!error){
				try{

					//Recebe mensagem para criacao de estruturas de pastas
                    Log.i("[INFO]","Aguardando conexão");//Mesagem mostrada no Logcat
                    mySocket = ss.accept();
                    is = mySocket.getInputStream();
                    Log.i("[INFO]","Conectado");


					if(is!=null) {
						bytesRead = is.read(aByte);
						Log.i("[INFO]", "bytesRead lenght: " + bytesRead);
						String message = new String(aByte).substring(0, bytesRead);
						message = message.substring(0, message.indexOf("\0"));
						Log.i("[INFO]", "Bytes content: " + message);




						mensagemSeparada = message.split("\n");
						handler.post(new Runnable() {
							@Override
							public void run() {
								getLocalizacao();
							}
						});
						nomeDoSuspeito = mensagemSeparada[0];
						idadeDoSuspeito = mensagemSeparada[1];
						nivelPerigoDoSuspeito = mensagemSeparada[2];
						listaDeCrimesDoSuspeito = mensagemSeparada[3].split(";");
						horaDeteccaoSuspeito = mensagemSeparada[4];
						probabilidadeDoSuspeito = mensagemSeparada[5];

						File mydir = context.getDir(nomeDoSuspeito, Context.MODE_PRIVATE);

						String timestampNome = nomeDoSuspeito + horaDeteccaoSuspeito;
						pathFrame = new File(mydir, timestampNome + ".bmp");
						pathFaceCrop = new File(mydir, timestampNome+ "_face_crop.bmp");
						pathMatchDataset = new File(mydir, timestampNome + "_best_match.bmp");
					}

					//Notificacao
					notifyAlert.alertNotification(context,"Alerta",nomeDoSuspeito+ " detectado!");

				}
				catch (IOException ex){
					ex.printStackTrace();
				}


					//Caminho do frame
					receiveData(pathFrame);
					//Caminho doadb face crop
					receiveData(pathFaceCrop);
					//Caminho do match dataset
					receiveData(pathMatchDataset);

					//Coloca tudo no ImageView e TextView
					handler.post(new Runnable() {
						@Override
						public void run() {
							File imgFileFrame = new  File(String.valueOf(pathFrame));
							File imgFileFaceCrop = new  File(String.valueOf(pathFaceCrop));
							File imgFileMatchDataset = new  File(String.valueOf(pathMatchDataset));

							if(imgFileFrame.exists()) {
								Bitmap bitmapFrame = BitmapFactory.decodeFile(imgFileFrame.getAbsolutePath());
								ImageView imageViewFrame = (ImageView) findViewById(R.id.imageFrame);
								imageViewFrame.setImageBitmap(bitmapFrame);
							}
							if (imgFileFaceCrop.exists()){
								Bitmap bitmapFaceCrop = BitmapFactory.decodeFile(String.valueOf(pathFaceCrop));
								ImageView imageViewFaceCrop = (ImageView) findViewById(R.id.imageFaceCrop);
								imageViewFaceCrop.setImageBitmap(bitmapFaceCrop);
							}
							if (imgFileMatchDataset.exists()){
								Bitmap bitmapMatchDataset = BitmapFactory.decodeFile(String.valueOf(pathMatchDataset));
								ImageView imageViewMatchDataset = (ImageView) findViewById(R.id.imageMatchDataset);
								imageViewMatchDataset.setImageBitmap(bitmapMatchDataset);
								hRostoSuspeito = bitmapMatchDataset;
							}

							TextView textViewName = (TextView) findViewById(R.id.textViewNameOfSuspectMain);
							textViewName.setText(String.format("%s: %s", getString(R.string.nome), nomeDoSuspeito));
							TextView textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracyMain);
							textViewAccuracy.setText(String.format("%s: %s", getString(R.string.acuracia), probabilidadeDoSuspeito));

							Detection d = new Detection(nomeDoSuspeito,idadeDoSuspeito,probabilidadeDoSuspeito,
									horaDeteccaoSuspeito,nivelPerigoDoSuspeito,
									BitmapFactory.decodeFile(String.valueOf(pathFrame)),
									BitmapFactory.decodeFile(String.valueOf(pathFaceCrop)),
									BitmapFactory.decodeFile(String.valueOf(pathMatchDataset)),
									new ArrayList<>(Arrays.asList(listaDeCrimesDoSuspeito)),latitude,longitude);
							if(d.imagem_frame != null || d.imagem_dataset != null || d.imagem_crop != null){
								database.insertAll(d);
							}
							buttonProfile.setVisibility(View.VISIBLE);

						}
					});

            }

            try {
            	if (ss != null) {
					ss.close();
				}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		private void receiveData(File path){

			try {
				Log.i("[INFO]","Aguardando conexão");//Mesagem mostrada no Logcat
				Socket mySocket = ss.accept();
				InputStream is = mySocket.getInputStream();
				Log.i("[INFO]","Conectado");

				if(is!=null) {

					fos = new FileOutputStream(path);
					bos = new BufferedOutputStream(fos);
					while ((bytesRead = is.read(aByte)) != -1) {
						bos.write(aByte, 0, bytesRead);
					}
                    bos.flush();
                    bos.close();
                    mySocket.close();
                }
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
    }

	public void getLocalizacao() {
		lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


		Log.d("LOCATION", "Requesting locations");
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			String[] locationPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
			ActivityCompat.requestPermissions(this, locationPermissions, 1);
			return;
		}
		if(!isGPSEnabled){
			new AlertDialog.Builder(this)
					.setTitle("Por favor ative os serviços de localização")
					.setPositiveButton("Ativar localização",new AlertDialog.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),1);
						}
					}).setIcon(R.drawable.ic_location_on_24px)
					.setCancelable(false)
					.show();
		}else {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			Location local = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (local != null){
				latitude = local.getLatitude();
				longitude = local.getLongitude();
			}
			else{
				//This is what you need:
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
			}

		}
	}
	@Override
	public void onLocationChanged(@NonNull Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("LOCATION", latitude + ":" + longitude);
		lm.removeUpdates(this);
	}




}

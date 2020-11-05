package com.example.apppolicial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class camera extends AppCompatActivity {
	private int ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE = 1;

	private BufferedReader in = null;
	private String dTextName = "";
	private Bitmap dBitmap;
	private Thread clientThread;
	private Bitmap hRostoSuspeito;
	private String nomeDoSuspeito;
	private String idadeDoSuspeito;
	private String nivelPerigoDoSuspeito;
	private String[] listaDeCrimesDoSuspeito;
	private String horaDeteccaoSuspeito;
	private String probabilidadeDoSuspeito;
	private Button buttonProfile;
	private String[] permissions = new String[] {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		//Verifica se app já tem permissão para gravar arquivos e localização
		ActivityCompat.requestPermissions(this,permissions , ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE);


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
		Thread myThread = new Thread(new MyServer(this));
		myThread.start();//Inicia thread de conexão via socket
	}

	public void goToForm(){
		Intent intent = new Intent (this, formulario.class);
		startActivity(intent);
	}

	public void goToHist(){
		Intent intent = new Intent (this, historico.class);
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

	public void goToPerf(){
		Intent intent = new Intent (this, perfil.class);
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
						Toast.makeText(getApplicationContext(), "Aguardando Conexao", Toast.LENGTH_LONG).show();
					}
				});//Acessa thread principal para exibir mensagem flutuante
			}catch (BindException ex){
				error =true;
			}
            catch (IOException ex){
				ex.printStackTrace();
				error=true;
			}

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
							textViewName.setText(String.format("%s %s", getString(R.string.name_main), nomeDoSuspeito));
							TextView textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracyMain);
							textViewAccuracy.setText(String.format("%s %s", getString(R.string.accuracy_main), probabilidadeDoSuspeito));
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE)  {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Obrigado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Precisamos dessas permissoes!", Toast.LENGTH_SHORT).show();
				ActivityCompat.requestPermissions(this,permissions , ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE);
			}
        }
    }


}

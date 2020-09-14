package com.example.apppolicial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    private BufferedReader in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            requestStoragePermission();//Caso não tenha requisita permissão (salvar as imagens)
        }//Verifica se app já tem permissão para gravar arquivos

        Thread myThread = new Thread(new MyServer(this));
        myThread.start();//Inicaia thread de conexão via socket
    }

    class MyServer implements  Runnable{
        ServerSocket ss;
        Socket mySocket;
        Handler handler = new Handler();//Usado para acessar a thread principal
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
			}catch (IOException ex){
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

                    //Notificacao
					notifyAlert.sendOnChannel1(context);

					if(is!=null) {
						bytesRead = is.read(aByte);
						Log.i("[INFO]", "bytesRead lenght: " + bytesRead);
						String message = new String(aByte).substring(0, bytesRead);
						message = message.substring(0, message.indexOf("\0"));
						Log.i("[INFO]", "Bytes content: " + message);

						mensagemSeparada = message.split("\n");

						File mydir = context.getDir(mensagemSeparada[0], Context.MODE_PRIVATE);
						String nome = mensagemSeparada[0] + mensagemSeparada[2];
						pathFrame = new File(mydir, nome + ".bmp");
						pathFaceCrop = new File(mydir, nome + "_face_crop.bmp");
						pathMatchDataset = new File(mydir, nome + "_best_match.bmp");
					}
				}catch (IOException ex){
					ex.printStackTrace();
				}


					//Caminho do frame
					receiveData(pathFrame);
					//Caminho do face crop
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
							}

							TextView textViewName = (TextView) findViewById(R.id.textViewNameOfSuspectMain);
							textViewName.append(mensagemSeparada[0]);

							TextView textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracyMain);
							textViewAccuracy.append(mensagemSeparada[2]);


						}
					});



            }


            try {
                ss.close();
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

					bytesRead = is.read(aByte);
					Log.i("[INFO]", "bytesRead lenght: " + bytesRead);
					String message = new String(aByte).substring(0, bytesRead);
					message = message.substring(0, message.indexOf("\0"));
					Log.i("[INFO]", "Bytes content: " + message);


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




    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

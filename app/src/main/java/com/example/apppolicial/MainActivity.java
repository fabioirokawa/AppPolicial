package com.example.wifitransfers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

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



        Thread myThread = new Thread(new MyServer());
        myThread.start();//Inicaia thread de conexão via socket
    }



    class MyServer implements  Runnable{
        ServerSocket ss;
        Socket mySocket;
        Handler handler = new Handler();//Usado para acessar a thread principal

        @Override
        public void run() {
            try {
                ss = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Waiting for client", Toast.LENGTH_SHORT).show();
                    }
                });//Acessa thread principal para exibir mensagem flutuante

                while (true){
                    System.out.println("Aguardando conexão");//Mesagem mostrada no Logcat
                    mySocket = ss.accept();
                    InputStream is = mySocket.getInputStream();
                    System.out.println("Conectado");

                    if (is!= null) {
                        FileOutputStream fos = null;
                        BufferedOutputStream bos = null;
                        try {
                            fos = new FileOutputStream("/storage/emulated/0/DCIM/Camera/000002.bmp");
                            bos = new BufferedOutputStream(fos);
                            byte[] aByte = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = is.read(aByte)) != -1) {
                                bos.write(aByte, 0, bytesRead);
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    File imgFile = new  File("/storage/emulated/0/DCIM/Camera/000002.bmp");

                                    if(imgFile.exists()){
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                                        myImage.setImageBitmap(myBitmap);
                                    }
                                }
                            });
                            ss.close();
                            bos.flush();
                            bos.close();
                            Log.i("IMSERVICE", "FILERECCC-2");

                        } catch (IOException ex) {
                            // Do exception handling
                        }
                    }
/*Transfere mesagem-------------------------------------------------------
                    System.out.println("Conectando");
                    mySocket = ss.accept();
                    in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                    try {
                        String message = "";
                        int charsRead = 0;
                        char[] buffer = new char[4096];

                        while ((charsRead = in.read(buffer)) != -1) {
                            message += new String(buffer).substring(0, charsRead);
                        }
                        System.out.println(message);
                    } catch (IOException e) {

                    }
--------------------------------------------------------------------------*/
                }
            } catch (IOException e) {
                e.printStackTrace();
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
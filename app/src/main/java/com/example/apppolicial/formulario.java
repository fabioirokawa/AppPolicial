package com.example.apppolicial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class formulario extends AppCompatActivity {

	private EditText crime;
	private AutoCompleteTextView peri;
	private EditText name;
	private Bitmap dBitmap = null;

	private NotifyAlert na = new NotifyAlert();
	private AlertDialog sendDialog;
	private Thread sendTread;

	static final int REQUEST_PHOTO_FROM_STORAGE = 1;
	static final int REQUEST_IMAGE_CAPTURE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario);
		final Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/policeDir/");
		sendDialog = new AlertDialog.Builder(this).setTitle("Enviando").setView(R.layout.my_dialog).setCancelable(false).create();
		name = findViewById(R.id.formName);
		crime = findViewById(R.id.formCrime);
		peri = findViewById(R.id.formPeri);

		String[] perigo = new String[]{"Baixo", "Medio", "Alto","Nivel de perigo"};
		ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this, R.layout.list_item,perigo){
			@Override
			public int getCount() {
				return super.getCount()-1;
			}
		};
		peri.setText(perigo[3],false);
		peri.setAdapter(adapter);
		Button buttonPegaFoto = findViewById(R.id.bPegaFoto);
		Button buttonTakePhoto = findViewById(R.id.bTakePhoto);
		Button buttonConfirma = findViewById(R.id.confirma_envio);
		Button buttonCancelaEnvio = findViewById(R.id.cancela_envio);
		buttonCancelaEnvio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		buttonConfirma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enviaNovoCadastro();
			}
		});
		buttonPegaFoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { //selecionar imagem do cel
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				//intent.setType("download/*");
				intent.setDataAndType(selectedUri, "image/*");
				startActivityForResult(Intent.createChooser(intent, "Selecione imagem"), REQUEST_PHOTO_FROM_STORAGE);
			}
		});
		buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

			}
		});
	}


	private void enviaNovoCadastro() {
		//perform verifications

		if (name.getText().toString().isEmpty()) {
			name.setError("Preencha todos os dados");

		}
		if (crime.getText().toString().isEmpty()) {
			crime.setError("Preencha todos os dados");

			return;
		}
		if (crime.getText().toString().isEmpty()) {
			crime.setError("Preencha todos os dados");
			return;
		}

		if(peri.getText().toString().equals("Nivel de perigo")){
			peri.setError("Preencha todos os dados");
			return;
		}
		if(dBitmap == null){
			Snackbar.make(findViewById(R.id.confirma_envio),"Defina uma foto", Snackbar.LENGTH_LONG).show();
			return;
		}



		Suspeito cadastro = new Suspeito(name.getText().toString(),crime.getText().toString(), peri.getText().toString(),dBitmap, 0.0, 0.0);
		sendTread = new Thread(new ClientThread(cadastro));
		sendTread.start();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			ImageView imageView = findViewById(R.id.iForm);
			switch (requestCode) {

				case REQUEST_IMAGE_CAPTURE:
					Bundle extras = data.getExtras();
					Bitmap bitmapCamera = (Bitmap) extras.get("data");
					dBitmap = bitmapCamera;
					imageView.setImageBitmap(bitmapCamera);
					break;
				case REQUEST_PHOTO_FROM_STORAGE:
					try {
						InputStream inputStream = getContentResolver().openInputStream(data.getData());
						Bitmap bitmapStorage = BitmapFactory.decodeStream(inputStream);
						dBitmap = bitmapStorage;
						imageView.setImageBitmap(bitmapStorage);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					break;

			}

		}
	}


	class ClientThread implements Runnable{
		Suspeito dados;
		String SERVER_SOCKET = "192.168.1.113"; //ip maquina
		int SERVER_PORT = 5001;


		ClientThread(Suspeito dados){
			this.dados = dados;
		}
		@Override
		public void run(){
			try {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sendDialog.show();
					}
				});
				InetAddress addr = InetAddress.getByName(SERVER_SOCKET);
				SocketAddress address= new InetSocketAddress(addr,SERVER_PORT);
				Socket socket = new Socket();
				socket.connect(address,5000);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				dados.getFotoDoSuspeito().compress(Bitmap.CompressFormat.PNG,100,stream);
				byte[] byteArray = stream.toByteArray();

				String texto = (byteArray.length + "/" + dados.getCrime() + "/" + "/" + dados.getPericulosidade() + "/" + dados.getNome()) ;

				na.progressNotification(formulario.this,"Enviando dados","Aguarde...");
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(texto.getBytes(StandardCharsets.UTF_8));
				outputStream.flush();


				DataOutputStream out = new DataOutputStream(outputStream);
				out.write(byteArray,0,byteArray.length );
				out.flush();

				Log.i("[INFO]","Arquivo enviado");
				socket.close();
				na.cancelNotification(formulario.this,2);
				na.alertNotification(formulario.this,"Finalizado","Dados enviados");
			}
			catch (UnknownHostException | SocketTimeoutException e){
				na.cancelNotification(formulario.this,2);
				na.errorNotification(formulario.this,"ERRO","Falha ao enviar");
				sendTread.interrupt();
			} catch (IOException e) {
				e.printStackTrace();
				na.cancelNotification(formulario.this,2);
				na.errorNotification(formulario.this,"ERRO","Falha ao enviar");
				sendTread.interrupt();
			}
			finally {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sendDialog.dismiss();
					}
				});
			}

		}

	}
}




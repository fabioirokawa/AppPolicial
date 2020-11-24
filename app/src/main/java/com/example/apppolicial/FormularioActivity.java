package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class FormularioActivity extends AppCompatActivity {

	private EditText crime;
	private AutoCompleteTextView peri;
	private EditText name;
	private EditText age;
	private Bitmap dBitmap = null;

	private final NotifyAlert na = new NotifyAlert();
	private AlertDialog sendDialog;
	private AlertDialog errorDialog;
	private Thread sendTread;

	static final int REQUEST_PHOTO_FROM_STORAGE = 1;
	static final int REQUEST_IMAGE_CAPTURE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario);
		final Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/policeDir/");
		sendDialog = new AlertDialog.Builder(this).setTitle(R.string.sending).setView(R.layout.my_dialog).setCancelable(false).create();
		errorDialog = new AlertDialog.Builder(this).setTitle(R.string.failed2send).setIcon(R.drawable.ic_baseline_error_24).setMessage(R.string.try_again).create();
		name = findViewById(R.id.formName);
		age = findViewById(R.id.formAge);
		crime = findViewById(R.id.formCrime);
		peri = findViewById(R.id.formPeri);

		String[] perigo = new String[]{"Baixo", "Medio", "Alto"};
		ArrayAdapter<String>  adapter = new ArrayAdapter<>(this, R.layout.list_item, perigo);
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
				startActivityForResult(Intent.createChooser(intent, getString(R.string.select_face)), REQUEST_PHOTO_FROM_STORAGE);
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

		int errCount=0;

		if (name.getText().toString().isEmpty()) {
			name.setError(getString(R.string.empty_field));
			errCount++;
		}
		if (crime.getText().toString().isEmpty()) {
			crime.setError(getString(R.string.empty_field));
			errCount++;
		}
		if (crime.getText().toString().isEmpty()) {
			crime.setError(getString(R.string.empty_field));
			errCount++;
		}
		if(age.getText().toString().isEmpty()) {
			age.setError(getString(R.string.empty_field));
			errCount++;
		}
		if(peri.getText().toString().isEmpty()){
			peri.setError(getString(R.string.empty_field));
			errCount++;
		}
		if(dBitmap == null){
			Snackbar.make(findViewById(R.id.confirma_envio), R.string.empty_photo, Snackbar.LENGTH_LONG).show();
			errCount++;
		}

		if(errCount > 0){
			return;
		}

		//TODO make an interface to collect all crimes, and make a list
		String[] crimes = new String[]{crime.getText().toString()};
		Suspeito cadastro = new Suspeito(name.getText().toString(), age.getText().toString(), crimes, peri.getText().toString(), dBitmap, 0.0, 0.0);
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

	 private void exitActivity(){
			finish();
	 }


	class ClientThread implements Runnable{
		Suspeito dados;
		String SERVER_SOCKET = "192.168.1.113"; //ip maquina
		int SERVER_PORT = 5001;


		ClientThread(Suspeito dados){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					sendDialog.show();
				}
			});
			this.dados = dados;
		}
		@Override
		public void run(){
			try {

				InetAddress addr = InetAddress.getByName(SERVER_SOCKET);
				SocketAddress address= new InetSocketAddress(addr,SERVER_PORT);
				Socket socket = new Socket();


				socket.connect(address,5001);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				dados.getFotoDoSuspeito().compress(Bitmap.CompressFormat.JPEG,100,stream);
				byte[] byteArray = stream.toByteArray();


				String[] crimes = dados.getCrimes();
				String tCrime = "";

				for(String cr : crimes){
					tCrime = ";"+cr;
				}
				String texto = (byteArray.length + "/" + tCrime + "/" + dados.getPericulosidade() + "/" + dados.getNome()+"/"+ dados.getIdadeDoSuspeito() + "&") ;

				na.progressNotification(FormularioActivity.this,"Enviando dados","Aguarde...");
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(texto.getBytes(StandardCharsets.UTF_8));



				DataOutputStream out = new DataOutputStream(outputStream);
				out.write(byteArray,0,byteArray.length );

				Log.i("[INFO]","Arquivo enviado");

				socket.close();
				out.flush();
				outputStream.flush();

				na.cancelNotification(FormularioActivity.this,2);
				na.successNotification(FormularioActivity.this,"Finalizado","Dados enviados");
				exitActivity();
			}
			catch (UnknownHostException | SocketTimeoutException e){
				Log.e("ENVIO","Timeout ou Host desconhecido");
				na.cancelNotification(FormularioActivity.this,2);
				na.errorNotification(FormularioActivity.this,"ERRO","Falha ao enviar");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						errorDialog.show();
					}
				});
			}catch (IOException e) {
				e.printStackTrace();
				na.cancelNotification(FormularioActivity.this,2);
				na.errorNotification(FormularioActivity.this,"ERRO","Falha ao enviar");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						errorDialog.show();
					}
				});
			}
			finally {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sendDialog.dismiss();
					}
				});
				sendTread.interrupt();

			}

		}

	}
}




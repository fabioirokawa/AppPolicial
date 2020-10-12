package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class formulario extends AppCompatActivity {

	private EditText crime;
	private NotifyAlert na = new NotifyAlert();

	private EditText name;
	private Bitmap dBitmap;
	private String sPeri;
	static final int REQUEST_PHOTO_FROM_STORAGE = 1;
	static final int REQUEST_IMAGE_CAPTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        final Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/policeDir/");
		name = findViewById(R.id.formName);
		crime = findViewById(R.id.formCrime);

		Spinner peri = findViewById(R.id.formPeri);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.level,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		peri.setAdapter(adapter);
		peri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				sPeri = adapterView.getItemAtPosition(i).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});

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




		Suspeito cadastro = new Suspeito(name.getText().toString(),crime.getText().toString(),sPeri,dBitmap);
		Thread sendTread = new Thread(new ClientThread(cadastro));
		na.progressNotification(this,"Enviando dados","Aguarde...");
		sendTread.start();
		try {
			sendTread.join();
			na.cancelNotification(this,2);
			na.alertNotification(this,"Finalizado","Dados enviados");
		} catch (InterruptedException e) {
			e.printStackTrace();
			na.errorNotification(this,"ERRO","Falha ao enviar");
		}

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
		Context context;
		String SERVER_SOCKET = "192.168.1.113";
		int SERVER_PORT = 5001;
		ClientThread(Suspeito dados){
			this.dados = dados;
		}
		@Override
		public void run() {
			try {
				Socket socket = new Socket(SERVER_SOCKET, SERVER_PORT);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				dados.getFotoDoSuspeito().compress(Bitmap.CompressFormat.PNG,100,stream);
				byte[] byteArray = stream.toByteArray();

				String texto = (byteArray.length + "/" + dados.getCrime() + "/" + "/" + dados.getPericulosidade() + "/" + dados.getNome()) ;

				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(texto.getBytes(StandardCharsets.UTF_8));
				outputStream.flush();


				DataOutputStream out = new DataOutputStream(outputStream);
				out.write(byteArray,0,byteArray.length  );
				out.flush();

				Log.i("[INFO]","Arquivo enviado");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

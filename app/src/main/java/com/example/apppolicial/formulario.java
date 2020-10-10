package com.example.apppolicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class formulario extends AppCompatActivity {

    private Bitmap dBitmap;
    static final int REQUEST_PHOTO_FROM_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        final Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/policeDir/");

        Button buttonPegaFoto = findViewById(R.id.bPegaFoto);

        buttonPegaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //selecionar imagem do cel

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("download/*");
                intent.setDataAndType(selectedUri, "image/*");

                startActivityForResult(Intent.createChooser(intent, "Selecione imagem"), REQUEST_PHOTO_FROM_STORAGE);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.iForm);
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmapStorage = BitmapFactory.decodeStream(inputStream);
                dBitmap = bitmapStorage;
                imageView.setImageBitmap(bitmapStorage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
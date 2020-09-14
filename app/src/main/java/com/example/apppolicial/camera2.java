package com.example.apppolicial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class camera2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        final Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/policeDir/");


        Button button = findViewById(R.id.bPhoto);
        Button button2 = findViewById(R.id.bCamHist);
        Button button3 = findViewById((R.id.bPerfil));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("download/*");
                intent.setDataAndType(selectedUri, "image/*");

                startActivityForResult(Intent.createChooser(intent, "Selecione imagem"), 1);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHist();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPerf();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            ImageView imageView = findViewById(R.id.iImagem);

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToHist(){
        Intent intent = new Intent (this, historico.class);
        startActivity(intent);
    }

    public void goToPerf(){
        Intent intent = new Intent (this, perfil.class);
        startActivity(intent);
    }

}
package com.example.conexaobluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciaBluetooth bluetoothInicializacao = new IniciaBluetooth();
        bluetoothInicializacao.obtemBluetoothAdapter();

        String dispName = "raspberrypi";
        BluetoothDevice deviceEncontrado = bluetoothInicializacao.procuraPareado(dispName);

        //Device == null se nao foi obtido o bt adapter ou nao encontrou o dispositivo requisitado
        if(deviceEncontrado != null)
        {
            IniciaBluetooth.ThreadDeConexao conexao = bluetoothInicializacao.new ThreadDeConexao(deviceEncontrado);
            conexao.start();
        }



    }
}
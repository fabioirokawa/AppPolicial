package com.example.conexaobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import static android.content.ContentValues.TAG;

import java.io.IOException;
import java.util.Set;

public class IniciaBluetooth  extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    //Construtor
    IniciaBluetooth()
    {

    }

    //Funcao que obtem o socket bluetooth e solicita ligar se nescessario
    public BluetoothAdapter obtemBluetoothAdapter()
    {
        final int REQUEST_ENABLE_BT = 1;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Obtendo BluetoothAdapter
        if (bluetoothAdapter == null) {
            //Dispositivo nao possui bluetooth
            return null;
        }

        //Ativando Bluetooth
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        //Esperando bluetooth ligar para o programa nao executar as funcoes dele antes de estar pronto
        while(true)
        {
            if(bluetoothAdapter.isEnabled())
            {
                break;
            }
        }

        this.bluetoothAdapter = bluetoothAdapter;
        return bluetoothAdapter;
    }

    public BluetoothDevice procuraPareado(String nome)
    {
        if (bluetoothAdapter == null)
        {
            return null;
        }

        //Tipo ArrayList, so que Set
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                if(deviceName.equals(nome))
                {
                    return device;
                }
            }
        }

        return null;
    }




    public class ThreadDeConexao extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final java.util.UUID MY_UUID = java.util.UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        public ThreadDeConexao(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            ServicosDoBluetooth bluetoothServ = new ServicosDoBluetooth();
            ServicosDoBluetooth.ConnectedThread conecta = bluetoothServ.new ConnectedThread(mmSocket);
            conecta.start();
            String strParaEnviar = "Teste Cel to Rasp";
            conecta.write(strParaEnviar.getBytes());
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}

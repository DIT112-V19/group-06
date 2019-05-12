package com.example.newspeechrproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {

    private TextView txvResult;


    BluetoothAdapter adapter;


    Intent enableBtIntent;
    int requestCodeForEnableBT;


    Set<BluetoothDevice> pairedDevices;
    BluetoothSocket btSocket;


    private final static int REQUEST_ENABLE_BT = 1;
    static final UUID uuid = UUID.fromString("da6b54ac-4595-4c28-a86b-bffcfe992134");

    //For car control sent to arduino
    private char carChar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txvResult);
        adapter = BluetoothAdapter.getDefaultAdapter();
        enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnableBT = 1;
        pairedDevices = adapter.getBondedDevices();








        enableBluetooth();
        ConnectBTDevice();






    }

    public void setCarChar(char carChar) {
        this.carChar = carChar;
    }


    public void run() {


        if (carChar == 'f') {


            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write(carChar);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (carChar == 'b') {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write(carChar);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    public String getDeviceName() {
        String deviceName = "";
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                 deviceName = device.getName();
            }
        }
        return deviceName;

    }

    public String getDeviceAddress() {
        String address = "";
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                address = device.getAddress();
            }
        }

        return address;
    }


    private void enableBluetooth() {
      if (adapter == null) {
          Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
      }
      if (!adapter.isEnabled()) {
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
          startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
          Toast.makeText(getApplicationContext(), "Bluetooth is enabled", Toast.LENGTH_SHORT).show();

      }



    }

    public void ConnectBTDevice() {

        String address = getDeviceAddress();
        String name = getDeviceName();
    try {


        BluetoothDevice car = adapter.getRemoteDevice(address);
        btSocket = car.createInsecureRfcommSocketToServiceRecord(uuid);
        btSocket.connect();
        Toast.makeText(getApplicationContext(), "Connected to: " + name, Toast.LENGTH_SHORT).show();
    }catch (IOException e) {
        Toast.makeText(getApplicationContext(), "Error while connecting to: " + name, Toast.LENGTH_SHORT).show();
    }



    }




    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "This device does not support speech input.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).contains("go") || result.get(0).contains("forward")) {
                        txvResult.setText("Going Forward");
                        setCarChar('f');
                        run();
                    } else if (result.get(0).contains("back") || result.get(0).contains("backwards")) {
                        txvResult.setText("Going Backwards");
                        setCarChar('b');
                        run();
                    } else if (result.get(0).contains("stop") || result.get(0).contains("halt") || result.get(0).contains("break")) {
                        txvResult.setText("Stopping");
                        run();
                    } else {
                        Toast.makeText(this, "Invalid Command. Please try again. :)", Toast.LENGTH_SHORT).show();
                    }

                }



        }


    }
}

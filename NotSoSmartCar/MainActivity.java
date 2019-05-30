package se.gu.student.notsosmartcar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    OutputStream Os;

    private TextView txvResult;

    BluetoothAdapter adapter;

    Intent enableBtIntent;
    private final static int requestCodeForEnableBT = 1;
    String deviceName = "DIT122";

    Set<BluetoothDevice> pairedDevices;
    BluetoothSocket btSocket;
    String btAddress = "20:17:03:02:01:88";

    private final static int REQUEST_ENABLE_BT = 1;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public final static String MODULE_MAC = "20:17:03:02:01:88";
    BluetoothDevice result;

    //For car control sent to arduino
    private char carChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = BluetoothAdapter.getDefaultAdapter();
        enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //requestCodeForEnableBT = 1;
        pairedDevices = adapter.getBondedDevices();

        enableBluetooth();

        ImageButton frontButton = (ImageButton)findViewById(R.id.frontButton);
        frontButton.setOnClickListener(this);

        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        Button stopButton = (Button)findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);

        Button devButton = (Button)findViewById(R.id.devButton);
        devButton.setOnClickListener(this);

        Button btButton = (Button)findViewById(R.id.btButton);
        btButton.setOnClickListener(this);

        Button linesBtn = (Button)findViewById(R.id.LinesBtn);
        linesBtn.setOnClickListener(this);

        Button avoidanceBtn = (Button)findViewById(R.id.AvoidanceBtn);
        avoidanceBtn.setOnClickListener(this);

        Button increaseBtn = (Button) findViewById(R.id.IncreaseBtn);

        Button decreaseBtn = (Button) findViewById(R.id.DecreaseBtn);

        Button FollowMeBtn = (Button) findViewById(R.id.FollowMeBtn);
        FollowMeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent dev;
        dev = new Intent(this, InfoActivity.class);

        switch (view.getId()){
            case R.id.devButton:
                MainActivity.this.startActivity(dev);
                break;

            case R.id.frontButton:
                Toast.makeText(getApplicationContext(),"Going Forward",Toast.LENGTH_SHORT).show();
                setCarChar('f');
                run();
                break;

            case R.id.backButton:
                Toast.makeText(getApplicationContext(),"Going Backward",Toast.LENGTH_SHORT).show();
                setCarChar('b');
                run();
                break;

            case R.id.stopButton:
                Toast.makeText(getApplicationContext(),"Stopping",Toast.LENGTH_SHORT).show();
                setCarChar('s');
                run();
                break;

            case R.id.btButton:
                setup();
                Toast.makeText(getApplicationContext(),"Car connected",Toast.LENGTH_SHORT).show();
                break;

            case R.id.AvoidanceBtn:
                Toast.makeText(getApplicationContext(), "Avoiding Obstacles", Toast.LENGTH_SHORT).show();
                setCarChar('c');
                run();
                break;

            case R.id.LinesBtn:
                Toast.makeText(getApplicationContext(), "Following Lines", Toast.LENGTH_SHORT).show();
                setCarChar('a');
                run();
                break;

            case R.id.IncreaseBtn:
                Toast.makeText(getApplicationContext(), "Increasing Speed", Toast.LENGTH_SHORT).show();
              //  setCarChar(''); uncomment when increase function is done in arduino code
                run();
                break;

            case R.id.FollowMeBtn:
                Toast.makeText(getApplicationContext(), "Following Target", Toast.LENGTH_SHORT).show();
                setCarChar('d');
                run();
                break;

            case R.id.DecreaseBtn:
                Toast.makeText(getApplicationContext(), "Decreasing speed", Toast.LENGTH_SHORT).show();
                //setCarChar(''); //Uncomment when decrease function is done in arduino code
                run();
                break;

            default:
                break;
        }
    }

    public void setCarChar(char carChar) {
        this.carChar = carChar;
    }

    public void setup(){
        BluetoothDevice car = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(btAddress);

        try {
            btSocket = car.createRfcommSocketToServiceRecord(uuid);

        }catch (IOException e) {
            e.printStackTrace();
        }

        try {

            btSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            Os = btSocket.getOutputStream();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error" + getDeviceAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    public void run() {

        if (btSocket != null) {
            try {
                Os.write(carChar);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error" + getDeviceAddress(), Toast.LENGTH_SHORT).show();
            }
            try {
                Os.flush();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error" + getDeviceAddress(), Toast.LENGTH_SHORT).show();

            }


            /*
            // Possibly add this while shutdown/crash of app
            try {
                btSocket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            */
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
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Enabling Bluetooth...", Toast.LENGTH_SHORT).show();
        }
    }

   /* public void getInput(){

        if()


    }*/

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

            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result.get(0).contains("go") || result.get(0).contains("forward")) {
                    Toast.makeText(getApplicationContext(), "Going Forward", Toast.LENGTH_LONG).show();
                    setCarChar('f');
                    run();
                } else if (result.get(0).contains("back") || result.get(0).contains("backwards")) {
                    Toast.makeText(getApplicationContext(), "Going Backward", Toast.LENGTH_LONG).show();
                    setCarChar('b');
                    run();
                } else if (result.get(0).contains("stop") || result.get(0).contains("halt") || result.get(0).contains("break")) {
                    Toast.makeText(getApplicationContext(), "Stopping", Toast.LENGTH_LONG).show();
                    setCarChar('s');
                    run();
                } else if (result.get(0).contains("go lines") || result.get(0).contains("follow lines") || result.get(0).contains("follow")) {
                    Toast.makeText(getApplicationContext(), "Following Line Forward", Toast.LENGTH_LONG).show();
                    setCarChar('a');
                    run();
                } else if (result.get(0).contains("follow me") || result.get(0).contains("me")) {
                    Toast.makeText(getApplicationContext(), "Following me", Toast.LENGTH_SHORT).show();
                    setCarChar('d');
                    run();
                } else if (result.get(0).contains("increase") || result.get(0).contains("increase speed")) {
                    Toast.makeText(getApplicationContext(), "Increasing speed", Toast.LENGTH_SHORT).show();
                  //  setCarChar('');
                    run();
                } else if (result.get(0).contains("decrease") || result.get(0).contains("decrease speed")) {
                    Toast.makeText(getApplicationContext(), "Decreasing speed", Toast.LENGTH_SHORT).show();
                    //setCarChar('');
                    run();
                }

                else {
                    Toast.makeText(this, "Invalid Command. Please try again. :)", Toast.LENGTH_SHORT).show();
                }
            }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Os.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error while closing", Toast.LENGTH_SHORT).show();

        }

        try {
            btSocket.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error while closing", Toast.LENGTH_SHORT).show();
        }

    }
}
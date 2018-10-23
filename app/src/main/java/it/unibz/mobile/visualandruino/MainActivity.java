package it.unibz.mobile.visualandruino;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class MainActivity extends Activity implements View.OnTouchListener {


    ViewGroup _root;
    private int _xDelta;
    private int _yDelta;
    private SmoothBluetooth mSmoothBluetooth;
    private List<Integer> mBuffer = new ArrayList<>();

    private SmoothBluetooth.Listener mListener = new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            //device does not support bluetooth
        }

        @Override
        public void onBluetoothNotEnabled() {
            //bluetooth is disabled, probably call Intent request to enable bluetooth
        }

        @Override
        public void onConnecting(Device device) {
            //called when connecting to particular device
            //Toast.makeText(MainActivity.this, "Connecting to: " + device.getName(), Toast.LENGTH_SHORT).show();
            //writeCommand("Connecting to: " + device.getName());
        }

        @Override
        public void onConnected(Device device) {
            //called when connected to particular device
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            //writeCommand("Connected");

        }

        @Override
        public void onDisconnected() {
            //called when disconnected from device
            //writeCommand("Disconnected");
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onConnectionFailed(Device device) {
            //called when connection failed to particular device
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onDiscoveryStarted() {
            //called when discovery is started
        }

        @Override
        public void onDiscoveryFinished() {
            //called when discovery is finished
        }

        @Override
        public void onNoDevicesFound() {
            //called when no devices found
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onDevicesFound(final List<Device> deviceList,
                                   final SmoothBluetooth.ConnectionCallback connectionCallback) {
            //receives discovered devices list and connection callback
            //you can filter devices list and connect to specific one
            //connectionCallback.connectTo(deviceList.get(position));

            Device spiderDevice = null;

            for (Device device: deviceList) {

                if(device.getName().equals("HC-05")) {
                    spiderDevice = device;
                }
            }

            connectionCallback.connectTo(spiderDevice);



        }

        @Override
        public void onDataReceived(int data) {
            //receives all bytes
            mBuffer.add(data);
            StringBuilder sb = new StringBuilder();

            if (data == 3 && !mBuffer.isEmpty()) {

                for (int integer : mBuffer) {
                    sb.append((char) integer);
                }
                mBuffer.clear();

                TextView resultView = findViewById(R.id.resultView);
                resultView.setText(sb.toString());

            }

        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSmoothBluetooth = new SmoothBluetooth(getApplicationContext());
        mSmoothBluetooth.setListener(mListener);
        mSmoothBluetooth.tryConnection();


        setContentView(R.layout.activity_main);


        _root = (ViewGroup)findViewById(R.id.root);

        ImageView  _view = (ImageView) findViewById(R.id.rectimage);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 200);
        layoutParams.leftMargin = 50;
        layoutParams.topMargin = 50;
        layoutParams.bottomMargin = -250;
        layoutParams.rightMargin = -250;
        _view.setLayoutParams(layoutParams);

        _view.setOnTouchListener(this);

        final Button buttonHigh = findViewById(R.id.digitalHigh);
        buttonHigh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText)findViewById(R.id.pinNumber);
                String pinNumber = edit.getText().toString();

                String testCommand = "3 " + pinNumber + " 1;";

                // Code here executes on main thread after user presses button
                mSmoothBluetooth.send(testCommand, false);
            }
        });

        final Button buttonLow = findViewById(R.id.digitalLow);
        buttonLow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText)findViewById(R.id.pinNumber);
                String pinNumber = edit.getText().toString();

                String testCommand = "3 " + pinNumber + " 0;";

                // Code here executes on main thread after user presses button
                mSmoothBluetooth.send(testCommand, false);
            }
        });


        final Button buttonAnalogRead = findViewById(R.id.analogReadButton);
        buttonAnalogRead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                String testCommand = "-1 41;";

                // Code here executes on main thread after user presses button
                mSmoothBluetooth.send(testCommand, false);
            }
        });

    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        _root.invalidate();
        return true;
    }}
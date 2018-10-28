package it.unibz.mobile.visualandruino.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;
import it.unibz.mobile.visualandruino.ListFragment;
import it.unibz.mobile.visualandruino.R;
import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.BrickTypes;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Value;


//Builder Class
public class BrickSender {

    // static variable single_instance of type Singleton
    private static BrickSender single_instance = null;

    private SmoothBluetooth mSmoothBluetooth;
    private List<Integer> mBuffer = new ArrayList<>();

    // static method to create instance of Singleton class
    public static BrickSender getInstance()
    {
        if (single_instance == null)
            single_instance = new BrickSender();

        return single_instance;
    }

    public void initiateBluetooth(Context context) {
        mSmoothBluetooth = new SmoothBluetooth(context);
        mSmoothBluetooth.setListener(mListener);
        mSmoothBluetooth.tryConnection();
    }


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

                //TextView resultView = getView().findViewById(R.id.resultView);
                //resultView.setText(sb.toString());

            }

        }
    };



    public void sendCommand(String command) {
        mSmoothBluetooth.send(command, false);
    }
}

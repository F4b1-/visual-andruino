package it.unibz.mobile.visualandruino.utils;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;
import it.unibz.mobile.visualandruino.ListFragment;
import it.unibz.mobile.visualandruino.models.InternalBrick;


//Builder Class
public class BrickCommunicator {

    // static variable single_instance of type Singleton
    private static BrickCommunicator single_instance = null;

    private SmoothBluetooth mSmoothBluetooth;
    private List<Integer> mBuffer = new ArrayList<>();

    private boolean awaitingReturn = false;
    private int currentReturnValue = -1;

    // static method to create instance of Singleton class
    public static synchronized BrickCommunicator getInstance()
    {
        if (single_instance == null) {
            single_instance = new BrickCommunicator();
        }

        return single_instance;
    }

    public void initiateBluetooth(Activity activity) {

        if(activity != null) {
            mSmoothBluetooth = new SmoothBluetooth(activity.getApplicationContext());
            mSmoothBluetooth.setListener(mListener);
            mSmoothBluetooth.tryConnection();

        }

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
            UiHelper.writeCommand("Connected to " + device.getName());

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
            UiHelper.writeCommand("Connection failed. Trying again...");
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
            UiHelper.writeCommand("No devices found. Trying again...");
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
           /* mBuffer.add(data);
            StringBuilder sb = new StringBuilder();

            if (data == 3 && !mBuffer.isEmpty()) {

                for (int integer : mBuffer) {
                    sb.append((char) integer);
                }
                mBuffer.clear();

                currentReturnValue = Integer.valueOf(sb.toString());
                awaitingReturn = false;



            }*/

           handleReceivedData(data);

        }
    };


    public void handleReceivedData(int data) {
        mBuffer.add(data);
        StringBuilder sb = new StringBuilder();

        if (data == 3 && !mBuffer.isEmpty()) {

            for (int integer : mBuffer) {
                sb.append((char) integer);
            }
            mBuffer.clear();

            setRequestedValue(sb.toString());
        }
    }

    public void setRequestedValue(String value) {
        value = value.substring(0, value.length() - 1);
        if(value != null && !value.isEmpty()) {
            currentReturnValue = Long.valueOf(value).intValue();
        }

        setAwaitingReturn(false);

    }



    public void sendCommand(String command) {
        mSmoothBluetooth.send(command, false);

    }

    public synchronized boolean isAwaitingReturn() {
        return awaitingReturn;
    }

    public synchronized void setAwaitingReturn(boolean awaitingReturn) {
        this.awaitingReturn = awaitingReturn;
    }

    public synchronized int getCurrentReturnValue() {
        return currentReturnValue;
    }

    public synchronized void setCurrentReturnValue(int currentReturnValue) {
        this.currentReturnValue = currentReturnValue;
    }

    public SmoothBluetooth getmSmoothBluetooth() {
        return mSmoothBluetooth;
    }
}

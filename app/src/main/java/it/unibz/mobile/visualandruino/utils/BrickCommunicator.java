package it.unibz.mobile.visualandruino.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;
import it.unibz.mobile.visualandruino.Constants;


//Builder Class
public class BrickCommunicator {

    // static variable single_instance of type Singleton
    private static BrickCommunicator single_instance = null;

    private SmoothBluetooth mSmoothBluetooth;
    private List<Integer> mBuffer = new ArrayList<>();

    private boolean awaitingReturn = false;
    private int currentReturnValue = -1;
    private Activity activity = null;

    private String bluetoothDeviceName = Constants.DEFAULT_BLUETOOTH_DEVICE;

    // static method to create instance of Singleton class
    public static synchronized BrickCommunicator getInstance()
    {
        if (single_instance == null) {
            single_instance = new BrickCommunicator();
        }

        return single_instance;
    }

    public void initiateBluetooth(Activity activity) {
        this.activity = activity;
        UiHelper.writeCommand("Initialising bluetooth");
        if(activity != null) {
            mSmoothBluetooth = new SmoothBluetooth(activity.getApplicationContext());
            mSmoothBluetooth.setListener(mListener);
            mSmoothBluetooth.tryConnection();

        }

    }

    public void initiateBluetooth(Activity activity, String bluetoothDeviceName) {
        this.bluetoothDeviceName = bluetoothDeviceName;
        initiateBluetooth(activity);

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
        }

        @Override
        public void onConnected(Device device) {
            UiHelper.writeCommand("Connected to " + device.getName());
        }

        @Override
        public void onDisconnected() {
            //called when disconnected from device
            UiHelper.writeCommand("You got disconnected. Go to the settings menu to reload.");

        }

        @Override
        public void onConnectionFailed(Device device) {
            //called when connection failed to particular device
            String deviceName = "device";
            if(device != null) {
                deviceName = device.getName();
            }
            UiHelper.writeCommand("Connection failed for " + deviceName +  ". Trying again...");
            if(activity != null && device != null) {
                mSmoothBluetooth.disconnect();
                mSmoothBluetooth.stop();
                initiateBluetooth(activity);

            }
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

            Device bluetoothDevice = null;

            for (Device device: deviceList) {

                if(device.getName().equals(bluetoothDeviceName)) {
                    bluetoothDevice = device;
                }
            }

            connectionCallback.connectTo(bluetoothDevice);



        }

        @Override
        public void onDataReceived(int data) {
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


    public SmoothBluetooth getmSmoothBluetooth() {
        return mSmoothBluetooth;
    }

    public void disconnect() {
        mSmoothBluetooth.disconnect();
    }

    public void stop() {
        mSmoothBluetooth.stop();
    }

    public String getBluetoothDeviceName() {
        return bluetoothDeviceName;
    }
}

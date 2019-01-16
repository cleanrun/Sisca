package com.siscaproject.sisca.Utilities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.siscaproject.sisca.BuildConfig;
import com.uk.tsl.rfid.DeviceListActivity;
import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;

import java.util.Timer;
import java.util.TimerTask;

public class TSLBluetoothDeviceActivity extends AppCompatActivity {
    // Debugging
    private static final String TAG = "TSLBTDeviceActivity";
    private static final boolean D = BuildConfig.DEBUG;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mDevice = null;

    protected AsciiCommander getCommander() {
        return ((TSLBluetoothDeviceApplication) getApplication()).getCommander();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Create the AsciiCommander to talk to the reader (if it doesn't already exist)
        if (getCommander() == null) {
            try {
                TSLBluetoothDeviceApplication app = (TSLBluetoothDeviceApplication) getApplication();
                AsciiCommander commander = new AsciiCommander(getApplicationContext());
                app.setCommander(commander);

            } catch (Exception e) {
                fatalError("Unable to create AsciiCommander");
            }
        }
    }


    // Terminate the app with the given message
    private void fatalError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                finish();
            }
        }, 1800);
    }

    protected void bluetoothNotAvailableError(String message) {
        fatalError(message);
    }


    @Override
    public void onStart() {
        super.onStart();

        // If no other attempt to connect is ongoing try to connect to last used reader
        // Note: When returning from the Device List activity
        if (mBluetoothAdapter.isEnabled()) {
            if (mDevice == null) {
                // Attempt to reconnect to the last reader used
                Toast.makeText(this, "Reconnecting to last used reader", Toast.LENGTH_SHORT).show();

                getCommander().connect(null);
            }
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        getCommander().disconnect();
        mDevice = null;
    }

    protected void connectToDevice(Intent deviceData, boolean secure) {
        Toast.makeText(this.getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
        // Get the device MAC address
        String address = deviceData.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        mDevice = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        if (mDevice != null) {
            getCommander().connect(mDevice);
        } else {
            if (D) Log.e(TAG, "Unable to obtain a Bluetooth device");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (D)
            Log.d(TAG, "selectDevice() onActivityResult: " + resultCode + " for request: " + requestCode);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectToDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectToDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "Bluetooth not enabled");
                    bluetoothNotAvailableError("Bluetooth was not enabled\nApplication Quitting...");
                }
        }
    }


    public void selectDevice() {
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }

    public void disconnectDevice() {
        mDevice = null;
        getCommander().disconnect();
    }

    public void reconnectDevice() {
        getCommander().connect(null);
    }
}

package com.siscaproject.sisca.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectionService {

    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "Sisca";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;

    Context mCOntext;

    private AcceptThread mInsecureAcceptThread;

    public BluetoothConnectionService(BluetoothAdapter mBluetoothAdapter, Context mCOntext) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mCOntext = mCOntext;
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch(IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            mServerSocket = tmp;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: AcceptThread running");
            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start..");
                // Wait until a connection is made
                socket = mServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection");
            }catch(IOException e){
                Log.e(TAG, "AcceptThread: IOException " + e.getMessage());
            }

        }
    }
}

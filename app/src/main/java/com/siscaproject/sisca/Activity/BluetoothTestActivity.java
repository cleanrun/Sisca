package com.siscaproject.sisca.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.siscaproject.sisca.Adapter.BluetoothListAdapter;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class BluetoothTestActivity extends AppCompatActivity{

    private static final String TAG = "BluetoothTestActivity";

    private BluetoothAdapter mBluetoothAdapter;

    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Yang dimasukin ke arraylist cuma BluetoothClass 1f00
                if(device.getBluetoothClass().toString().equals("1f00")){
                    listDevice.add(device);
                }

                Log.d(TAG, "onReceive: " + device.getName() + ", " + device.getAddress());

                rv_list_device.setHasFixedSize(true);
                rv_list_device.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                BluetoothListAdapter listAdapter = new BluetoothListAdapter(listDevice,
                        BluetoothTestActivity.this, mBluetoothAdapter);
                rv_list_device.setAdapter(listAdapter);
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND BONDED");
                }
                else if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "BroadcastReceiver: BOND BONDING");
                }
                else if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "BroadcastReceiver: BOND NONE");
                }
            }
        }
    };

    @BindView(R.id.sw_bluetooth) SwitchCompat sw_bluetooth;

    @BindView(R.id.rv_list_device) RecyclerView rv_list_device;
    @BindView(R.id.srl_list_parent) SwipeRefreshLayout srl_list_parent;

    @BindView(R.id.tv_btWarning) TextView tv_btWarning;

    private BluetoothListAdapter rvAdapter;
    private ArrayList<BluetoothDevice> listDevice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        ButterKnife.bind(this);

        rv_list_device.setHasFixedSize(true);
        rv_list_device.setLayoutManager(new LinearLayoutManager(this));

        rvAdapter = new BluetoothListAdapter(listDevice, BluetoothTestActivity.this, mBluetoothAdapter);
        rv_list_device.setAdapter(rvAdapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver3, filter);

        listDevice = new ArrayList<>();

        srl_list_parent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDevice();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_list_parent.setRefreshing(false);
                        mBluetoothAdapter.cancelDiscovery();
                        Log.d(TAG, "onRefresh: Canceling discovery");
                    }
                }, 20000);
            }
        });


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initialSwitch();
    }

    private void initialSwitch(){
        if(mBluetoothAdapter.isEnabled()){
            sw_bluetooth.setChecked(true);
            srl_list_parent.setVisibility(View.VISIBLE);
            tv_btWarning.setVisibility(View.GONE);
        }else if(!mBluetoothAdapter.isEnabled()){
            sw_bluetooth.setChecked(false);
            srl_list_parent.setVisibility(View.GONE);
            tv_btWarning.setVisibility(View.VISIBLE);
        }
    }

    public void refreshDevice(){
        listDevice.clear();

        Log.d(TAG, "refreshDevice: Looking for unpaired devices");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "refreshDevice: Canceling discovery");

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver2, discoverDevicesIntent);
        }
        else if(!mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver2, discoverDevicesIntent);
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();

        try {
            if(mBroadcastReceiver1 != null){
                unregisterReceiver(mBroadcastReceiver1);
                mBroadcastReceiver1 = null;
            }
        }catch(RuntimeException e){
            Log.e(TAG, e.getMessage());
        }

        try {
            if(mBroadcastReceiver2 != null){
                unregisterReceiver(mBroadcastReceiver2);
                mBroadcastReceiver2 = null;
            }
        }catch(RuntimeException e){
            Log.e(TAG, e.getMessage());
        }

        try {
            if(mBroadcastReceiver3 != null){
                unregisterReceiver(mBroadcastReceiver3);
                mBroadcastReceiver3 = null;
            }
        }catch(RuntimeException e){
            Log.e(TAG, e.getMessage());
        }

        finish();
    }

    @OnCheckedChanged({R.id.sw_bluetooth})
    public void onSwitch(){
        Log.d(TAG, "onSwitch: enabling/disabling bluetooth");

        if(sw_bluetooth.isChecked() == true){
            Log.d(TAG, "enableDisableBluetooth: enabling bluetooth");
            //Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivity(enableBTIntent);
            srl_list_parent.setVisibility(View.VISIBLE);
            tv_btWarning.setVisibility(View.GONE);
            mBluetoothAdapter.enable();
            listDevice.clear();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        else if(sw_bluetooth.isChecked() == false){
            Log.d(TAG, "enableDisableBluetooth: disabling bluetooth");
            mBluetoothAdapter.disable();
            srl_list_parent.setVisibility(View.GONE);
            tv_btWarning.setVisibility(View.VISIBLE);
            listDevice.clear();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        else if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBluetooth: Doesn't have BT capabilities");
        }
    }

}

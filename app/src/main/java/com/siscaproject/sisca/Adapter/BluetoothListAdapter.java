package com.siscaproject.sisca.Adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.itemHolder>{

    private static final String TAG = "BluetoothListAdapter";

    private ArrayList<BluetoothDevice> listDevice;
    private Context activityContext;

    private BluetoothAdapter bluetoothAdapter;

    public BluetoothListAdapter(ArrayList<BluetoothDevice> listDevice,
                                Context activityContext, BluetoothAdapter bluetoothAdapter) {
        this.listDevice = listDevice;
        this.activityContext = activityContext;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_list_device, parent, false);
        return new itemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemHolder holder, final int position) {
        holder.tv_device_name.setText(listDevice.get(position).getName());
        holder.tv_device_address.setText(listDevice.get(position).getAddress());

        holder.ll_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pair
                Log.d(TAG, "onClick: Clicked on a device");

                MaterialDialog builder = new MaterialDialog.Builder(activityContext)
                        .title("Pair")
                        .content("Pair with this device?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                bluetoothAdapter.cancelDiscovery();

                                String deviceName = listDevice.get(position).getName();
                                String deviceAddress = listDevice.get(position).getAddress();
                                String deviceType = listDevice.get(position).getBluetoothClass().toString();

                                Log.d(TAG, "onClick: " + deviceName + ", " + deviceAddress + ", " + deviceType);

                                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                                    Log.d(TAG, "Pairing with a device " + deviceName);
                                    listDevice.get(position).createBond();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // do nothing
                                Log.d(TAG, "onClick: Do nothing");
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  listDevice == null ? 0 : listDevice.size();
    }

    public class itemHolder extends RecyclerView.ViewHolder{
        // Doesn't work with ButterKnife

        private LinearLayout ll_parent_layout;
        private TextView tv_device_name;
        private TextView tv_device_address;

        public itemHolder(View itemView) {
            super(itemView);
            ll_parent_layout = itemView.findViewById(R.id.ll_parent_layout);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            tv_device_address = itemView.findViewById(R.id.tv_device_address);
        }
    }

}

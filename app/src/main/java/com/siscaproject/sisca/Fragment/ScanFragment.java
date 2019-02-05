package com.siscaproject.sisca.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.Activity.AssetsListCategoryActivity;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.Activity.FormNewAssetActivity;
import com.siscaproject.sisca.Model.PairedDevice;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.BluetoothConnector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanFragment extends Fragment {
    private static final String TAG = "ScanFragment";

    final int handlerState = 0;

    private BluetoothDevice device = null;
    private BluetoothConnector.BluetoothSocketWrapper mSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    BluetoothConnector bluetoothConnector;
    private Handler bluetoothIn;
    private PairedDevice pairedDevice;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    @BindView(R.id.tv_total_data) TextView totalData;
    @BindView(R.id.fab_register_data) FloatingActionButton registerData;
    @BindView(R.id.parent_register) FrameLayout parentLayout;

    private MaterialDialog createDialog;

    // The list of results from actions
    private ArrayAdapter<String> mResultsArrayAdapter;
    private ListView mResultsListView;

    private ArrayList<String> data = new ArrayList<>();

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(this, view);

        mResultsArrayAdapter = new ArrayAdapter<>(ScanFragment.this.getContext(), android.R.layout.simple_list_item_1);

        mResultsArrayAdapter.add("1234");

        mResultsListView = view.findViewById(R.id.lv_data);
        mResultsListView.setAdapter(mResultsArrayAdapter);
        mResultsListView.setFastScrollEnabled(true);

        mResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //mResultsArrayAdapter.remove(mResultsArrayAdapter.getItem(position));
                //mResultsArrayAdapter.notifyDataSetChanged();
                //totalData.setText(Integer.toString(mResultsArrayAdapter.getCount()) + " items detected");

                showCreateDialog(mResultsArrayAdapter.getItem(position));
            }
        });

        return view;
    }

    public void addData(String message){
        if(mResultsArrayAdapter.getCount() == 0){
            mResultsArrayAdapter.add(message);
        }
        else{
            boolean exists = false;
            for(int i = 0; i < mResultsArrayAdapter.getCount(); i++){
                if(message.equals(mResultsArrayAdapter.getItem(i))){
                    exists = true;
                    break;
                }
            }
            if(!exists){
                mResultsArrayAdapter.add(message);
            }
        }
        totalData.setText(Integer.toString(mResultsArrayAdapter.getCount()) + " items detected");
        scrollResultsListViewToBottom();
    }

    private void scrollResultsListViewToBottom(){
        mResultsListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view
                mResultsListView.setSelection(mResultsArrayAdapter.getCount() - 1);
            }
        });
    }

    @OnClick(R.id.fab_register_data)
    public void onClick(){
        Snackbar.make(parentLayout, "Register Data", Snackbar.LENGTH_SHORT).show();
    }

    private void showCreateDialog(final String tag){
        Log.i(TAG, "showCreateDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .content("Do you want to register this asset?")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getActivity(), FormNewAssetActivity.class);
                        intent.putExtra("asset_tag", tag);
                        startActivity(intent);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // Do nothing
                    }
                })
                .canceledOnTouchOutside(true);

        createDialog = builder.build();
        createDialog.show();
    }
}

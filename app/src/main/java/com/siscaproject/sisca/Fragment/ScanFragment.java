package com.siscaproject.sisca.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.PairedDevice;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.BluetoothConnector;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends Fragment{
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
    @BindView(R.id.parent_register) FrameLayout parentLayout;

    private MaterialDialog createDialog;
    private MaterialDialog infoDialog;

    private UserService userService;

    // The list of results from actions
    private ArrayAdapter<String> mResultsArrayAdapter;
    private ListView mResultsListView;

    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<AssetMutasi> listAsset;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = APIProperties.getUserService();
        getAsset();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(this, view);

        mResultsArrayAdapter = new ArrayAdapter<>(ScanFragment.this.getContext(), android.R.layout.simple_list_item_1);

        mResultsArrayAdapter.add("0000");
        mResultsArrayAdapter.add("7163");

        mResultsListView = view.findViewById(R.id.lv_data);
        mResultsListView.setAdapter(mResultsArrayAdapter);
        mResultsListView.setFastScrollEnabled(true);

        totalData.setText(Integer.toString(mResultsArrayAdapter.getCount()) + " items detected");

        mResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //mResultsArrayAdapter.remove(mResultsArrayAdapter.getItem(position));
                //mResultsArrayAdapter.notifyDataSetChanged();
                //totalData.setText(Integer.toString(mResultsArrayAdapter.getCount()) + " items detected");
                //isAssetExist(mResultsArrayAdapter.getItem(position));
                //showCreateDialog(mResultsArrayAdapter.getItem(position));
                //showDialog(mResultsArrayAdapter.getItem(position));
                try{
                    String s = mResultsArrayAdapter.getItem(position);
                    showDialog(s);
                    Log.i(TAG, "onItemClick: " + s);
                }catch(Exception e){
                    Log.e(TAG, "onItemClick: " + e.getMessage() );
                    showToast("Error occurred");
                }
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
                        /*
                        Intent intent = new Intent(getActivity(), FormNewAssetActivity.class);
                        intent.putExtra("asset_tag", tag);
                        startActivity(intent);
                        */
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


    private void showInfoDialog(final AssetMutasi asset){
        Log.i(TAG, "showInfoDialog: called");

        String name = "Name : " + asset.getName();
        //String tag = "AssetModel Tag : " + asset.getAsset_id();
        String tag = "AssetModel Tag : " + asset.getAsset_rfid();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .title("AssetModel Registered!")
                .content(name + '\n' + tag)
                .contentGravity(GravityEnum.START)
                .autoDismiss(true)
                .neutralText("Dismiss")
                .positiveText("Details")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            /*
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            ShowAssetFragment fragment = new ShowAssetFragment();
                            fragment.setAsset(asset);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
                            */
                        }catch(Exception e){
                            Log.e(TAG, "onClick: " + e.getMessage() );
                        }
                    }
                })
                .canceledOnTouchOutside(true);

        infoDialog = builder.build();
        infoDialog.show();
    }

    private Boolean isAssetExist(String assetTag){
        for(AssetMutasi a : listAsset){
            String tag = a.getAsset_id();
            if (tag.equals(assetTag)){
                Log.i(TAG, "isAssetExist: true");
                return true;
            }
        }
        Log.i(TAG, "isAssetExist: false");
        return false;
    }

    private void showDialog(String tag){
        Boolean a = isAssetExist(tag);

        if(a.equals(true)){
            //showInfoDialog(a);
            showToast("true");
        }
        else{
            //showCreateDialog(tag);
            showToast("false");
        }
    }


    private void getAsset() {
        Call<ResponseIndex<AssetMutasi>> call = userService.indexAssetMutasi(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetMutasi>> call, Response<ResponseIndex<AssetMutasi>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: total " + response.body().getTotal());
                    listAsset = response.body().getData();
                    Log.i(TAG, "onResponse: listAsset size " + listAsset.size());
                    Log.i(TAG, "onResponse: listAsset rfid random index == " + listAsset.get(4).getAsset_rfid());
                    Log.i(TAG, "onResponse: listAsset assetid random index == " + listAsset.get(4).getAsset_id());
                    Log.i(TAG, "onResponse: listAsset name random index == " + listAsset.get(4).getName());
                }
                else{
                    Log.e(TAG, "getAsset onResponse: not successful");
                    showToast("Unable to get Asset data");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetMutasi>> call, Throwable t) {
                Log.e(TAG, "getAsset onFailure: " + t.getMessage() );
                showToast("Unable to get Asset data");
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}

package com.siscaproject.sisca.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Activity.ReportMonitoringActivity;
import com.siscaproject.sisca.Adapter.MonitoringDetailAdapter;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.PairedDevice;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.User;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.BluetoothConnector;
import com.siscaproject.sisca.Utilities.Config;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BluetoothMonitoringFragment extends Fragment{
    private static final String TAG = "BluetoothMonitoringFrag";

    final int handlerState = 0;

    private BluetoothDevice device = null;
    private BluetoothConnector.BluetoothSocketWrapper mSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    BluetoothConnector bluetoothConnector;
    private Handler bluetoothIn;
    private PairedDevice pairedDevice;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    //@BindView(R.id.tv_title_report_detail) TextView tvTitle;
    @BindView(R.id.tv_date_report_detail) TextView tvDate;
    @BindView(R.id.tv_time_report_detail) TextView tvTime;
    @BindView(R.id.tv_pic_report_detail) TextView tvPic;
    @BindView(R.id.rv_report_detail) RecyclerView recyclerView;
    @BindView(R.id.pv_report_detail) ProgressView progressView;
    //@BindView(R.id.ll_empty_report_detail) LinearLayout llEmpty;
    @BindView(R.id.btn_submit_report_detail) Button btnSubmit;

    private ArrayList<String> detectedData;

    private UserService userService;
    private List<AssetAPI> assetAPIList;
    private String idLocation;
    private boolean isFormReportActivity;
    private MonitoringDetailAdapter adapter;
    private int indexUpdate;
    private LocationAPI locationAPI;
    private ReportMonitoringActivity reportMonitoringActivity;

    public void setReportMonitoringActivity(ReportMonitoringActivity reportMonitoringActivity){
        this.reportMonitoringActivity = reportMonitoringActivity;
    }

    public BluetoothMonitoringFragment() {
        // Required empty public constructor
    }

    public static BluetoothMonitoringFragment newInstance() {
        BluetoothMonitoringFragment fragment = new BluetoothMonitoringFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = APIProperties.getUserService();
        detectedData = new ArrayList<>();

        try{
            idLocation = getActivity().getIntent().getIntExtra("ID_LOCATION_EXTRA", 0)+"";
            isFormReportActivity = getActivity().getIntent().getBooleanExtra("FROM_REPORT_EXTRA", false);
        }catch(Exception e){
            Log.e(TAG, "onCreate: " + e.getMessage() );
            showToast("Extras not fetched");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth_monitoring, container, false);
        ButterKnife.bind(this, view);

        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        getLocation();

        return view;
    }

    public void addData(String message){
        try{
            String subMessage = message.substring(5);
            if(detectedData.isEmpty()){
                Log.i(TAG, "addData: added " + message);
                //detectedData.add(message);
                detectedData.add(subMessage);
            }else{
                boolean exists = false;
                for(String a : detectedData){
                    //if(message.equals(a)){
                    if(subMessage.equals(a)){
                        Log.i(TAG, "addData: data " + message + " already exists");
                        exists = true;
                        break;
                    }
                }
                if(!exists){
                    Log.i(TAG, "addData: added " + message);
                    //detectedData.add(message);
                    detectedData.add(subMessage);
                }
            }
        }catch(Exception e){
            Log.e(TAG, "addData: " + e.getMessage() );
            showToast("Something wrong happened :(");
        }

        checkData();
    }

    public void checkData(){
        for(AssetAPI a : assetAPIList){
            if(a.getCondition().equals("bagus")){
                a.setCondition("rusak");
            }
            else if(a.getCondition().equals("rusak")){
                a.setCondition("bagus");
            }
        }
        showData();
    }

    @OnClick(R.id.btn_submit_report_detail)
    public void btnSubmitClick() {
        progressView.setVisibility(View.VISIBLE);
        assetAPIList = adapter.getList();
        indexUpdate=0;
        updateData();

    }

    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void getLocation(){
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    List<LocationAPI> data = response.body().getData();

                    for(int i=0; i < data.size(); i++){
                        String id = data .get(i).getId()+"";
                        if(id.equals(idLocation)){
                            locationAPI = data.get(i);
                            //tvTitle.setText(locationAPI.getName());
                            tvDate.setText(locationAPI.getUpdated_at().substring(0, 10));
                            tvTime.setText(locationAPI.getUpdated_at().substring(11));

                            getPicName(locationAPI.getManager_id());
                        }
                    }

                    getAsset();
                }else{
                    Log.e(TAG, "onResponse: else");
                    showToast("Something wrong happened :(");
                    getAsset();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
                showToast("Something wrong happened :(");
                getAsset();
            }
        });
    }

    private void getPicName(final int managerId){
        Call<ResponseIndex<User>> call = userService.indexUser(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<User>>() {
            @Override
            public void onResponse(Call<ResponseIndex<User>> call, Response<ResponseIndex<User>> response) {
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    List<User> data = response.body().getData();

                    for(int i=0; i<data.size(); i++){
                        if(data.get(i).getId() == managerId){
                            tvPic.setText(data.get(i).getName());
                        }
                    }
                }else{
                    Log.e(TAG, "onResponse: else");
                    showToast("Something wrong happened :(");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<User>> call, Throwable t) {
                Log.e(TAG, "onResponse: else");
                showToast("Something wrong happened :(");
            }
        });
    }

    private void getAsset(){
        Call<ResponseIndex<AssetAPI>> call = userService.indexAsset(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetAPI>> call, Response<ResponseIndex<AssetAPI>> response) {
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    assetAPIList = response.body().getData();

                    Collections.sort(assetAPIList, new Comparator<AssetAPI>() {
                        @Override
                        public int compare(AssetAPI assetAPI, AssetAPI t1) {
                            return assetAPI.getName().compareTo(t1.getName());
                        }
                    });

                    List<AssetAPI> tmpList = new ArrayList<>();
                    for(int i=0; i<assetAPIList.size(); i++){
                        if(assetAPIList.get(i).getLocation_id().equals(idLocation)){
                            tmpList.add(assetAPIList.get(i));
                        }
                    }

                    assetAPIList.clear();
                    assetAPIList = tmpList;

                    showData();
                }else{
                    Log.e(TAG, "onResponse: else");
                    showToast("Something wrong happened :(");
                    showData();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetAPI>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
                showToast("Something wrong happened :(");
                showData();
            }
        });
    }

    public void showData(){
        adapter = new MonitoringDetailAdapter(getActivity(), assetAPIList);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    private void updateData(){
        String dateNow = Config.getDateNow();
        for(int i=0; i<assetAPIList.size(); i++){
            assetAPIList.get(i).setUpdated_at(dateNow);
        }

        if(indexUpdate<assetAPIList.size()){
            Call<AssetAPI> call = userService.putAsset(Header.auth, Header.accept,
                    assetAPIList.get(indexUpdate).getId(), assetAPIList.get(indexUpdate));
            call.enqueue(new Callback<AssetAPI>() {
                @Override
                public void onResponse(Call<AssetAPI> call, Response<AssetAPI> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: success " + indexUpdate);
                        indexUpdate++;
                        updateData();
                    }else{
                        try{
                            Log.e(TAG, "onResponse: gagal " + indexUpdate );
                            Log.e(TAG, "onResponse: gagal " + response.errorBody().string() );
                            showToast("Something wrong happened :(");
                        }catch(Exception e){
                            Log.e(TAG, "onResponse: " + e.getMessage() );
                            showToast("Something wrong happened :(");
                        }
                    }
                }
                @Override
                public void onFailure(Call<AssetAPI> call, Throwable t) {

                }
            });

        }else{
            locationAPI.setUpdated_at(dateNow);
            updateDataLocation();
        }
    }

    private void updateDataLocation(){
        Call<LocationAPI> call = userService.putLocation(Header.auth, Header.accept, locationAPI.getId(), locationAPI);
        call.enqueue(new Callback<LocationAPI>() {
            @Override
            public void onResponse(Call<LocationAPI> call, Response<LocationAPI> response) {
                if(response.isSuccessful()){
                    showToast("Data successfully inputted.");
                    Log.i(TAG, "onResponse: success " + locationAPI.getUpdated_at());
                    assetAPIList.clear();
                    getLocation();
                }else{
                    Log.e(TAG, "onResponse: not successful " + locationAPI.getUpdated_at());
                    showToast("Unable to update the data");
                    assetAPIList.clear();
                    getLocation();
                }
            }
            @Override
            public void onFailure(Call<LocationAPI> call, Throwable t) {
                Log.e(TAG, "onResponse: not successful " + locationAPI.getUpdated_at());
                showToast("Unable to update the data");
                assetAPIList.clear();
                getLocation();
            }
        });
    }

}

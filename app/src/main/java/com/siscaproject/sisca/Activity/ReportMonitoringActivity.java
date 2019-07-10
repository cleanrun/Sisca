package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.User;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
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

public class ReportMonitoringActivity extends AppCompatActivity {

    @BindView(R.id.pv_report_report)
    ProgressView progressView;
    @BindView(R.id.tv_title_report_report)
    TextView tvTitle;
    @BindView(R.id.tv_date_report_report)
    TextView tvDate;
    @BindView(R.id.tv_time_report_report)
    TextView tvTime;
    @BindView(R.id.tv_pic_report_report)
    TextView tvPic;
    @BindView(R.id.tv_asset_size_report_report)
    TextView tvAssetSize;
    @BindView(R.id.tv_bagus_report_report)
    TextView tvBagus;
    @BindView(R.id.tv_rusak_report_report)
    TextView tvRusak;
    @BindView(R.id.tv_hilang_report_report)
    TextView tvHilang;

    private UserService userService;
    private List<AssetAPI> assetAPIList;
    private String idLocation;
    private LocationAPI locationAPI;
    private int bagusSize=0, rusakSize=0, hilangSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_monitoring);

        ButterKnife.bind(this);

        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        idLocation = getIntent().getIntExtra("ID_LOCATION_EXTRA", 0)+"";

        userService = APIProperties.getUserService();
        getLocation();
    }

    private void getLocation() {
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    List<LocationAPI> data = response.body().getData();

                    for (int i=0; i<data.size(); i++){
                        String id = data.get(i).getId()+"";
                        if (id.equals(idLocation)){
                            locationAPI = data.get(i);

                            tvTitle.setText(locationAPI.getName());
                            tvDate.setText(locationAPI.getUpdated_at().substring(0, 10));
                            tvTime.setText(locationAPI.getUpdated_at().substring(11));
                            tvAssetSize.setText(locationAPI.getAsset_count()+"");

                            getPicName(locationAPI.getManager_id());
                        }
                    }

                    getAsset();
                } else {
                    Log.i(Constraints.TAG, "onResponse: else");
                    errorToast();
                    getAsset();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                errorToast();
                getAsset();
            }
        });
    }

    private void getPicName(final int manager_id) {
        Call<ResponseIndex<User>> call = userService.indexUser(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<User>>() {
            @Override
            public void onResponse(Call<ResponseIndex<User>> call, Response<ResponseIndex<User>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    List<User> data = response.body().getData();

                    for (int i=0; i<data.size(); i++){
                        if (data.get(i).getId()==manager_id){
                            tvPic.setText(data.get(i).getName());
                        }
                    }
                } else {
                    Log.i(Constraints.TAG, "onResponse: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<User>> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                errorToast();
            }
        });
    }

    private void getAsset(){
        Call<ResponseIndex<AssetAPI>> call = userService.indexAsset(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetAPI>> call, Response<ResponseIndex<AssetAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    assetAPIList = response.body().getData();

                    Collections.sort(assetAPIList, new Comparator<AssetAPI>() {
                        @Override
                        public int compare(AssetAPI assetAPI1, AssetAPI assetAPI2) {
                            return assetAPI1.getName().compareTo(assetAPI2.getName());
                        }
                    });

                    //seleksi aset sesuai lokasi
                    List<AssetAPI> tmpList = new ArrayList<>();
                    for (int i=0; i<assetAPIList.size(); i++){
                        if (assetAPIList.get(i).getLocation_id().equals(idLocation)){
                            tmpList.add(assetAPIList.get(i));
                            if (assetAPIList.get(i).getCondition().equals("bagus"))
                                bagusSize++;
                            else if (assetAPIList.get(i).getCondition().equals("rusak"))
                                rusakSize++;
                            else if (assetAPIList.get(i).getCondition().equals("hilang"))
                                hilangSize++;
                        }
                    }

                    assetAPIList.clear();
                    assetAPIList = tmpList;

                    tvBagus.setText(bagusSize+"");
                    tvRusak.setText(rusakSize+"");
                    tvHilang.setText(hilangSize+"");

                    progressView.setVisibility(View.INVISIBLE);
                } else {
                    Log.i(Constraints.TAG, "onResponse: else");
                    errorToast();
                    progressView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetAPI>> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                errorToast();
                progressView.setVisibility(View.INVISIBLE);
            }
        });


    }

    @OnClick(R.id.cv_back_report_report) void cvBackOnClick(){
        onBackPressed();
    }

    @OnClick(R.id.btn_edit_report_report) void btnEditOnClick(){
        //DetailMonitoringActivity activity = new DetailMonitoringActivity();
        BluetoothMonitoringActivity activity = new BluetoothMonitoringActivity();
        activity.setReportMonitoringActivity(ReportMonitoringActivity.this);
        Intent intent = new Intent(this, activity.getClass());
        intent.putExtra("ID_LOCATION_EXTRA", locationAPI.getId());
        intent.putExtra("FROM_REPORT_EXTRA", true);
        intent.putExtra("LOCATION_NAME_EXTRA", locationAPI.getName());

        startActivity(intent);
        finish();
    }

    public void load(){
        progressView.setVisibility(View.VISIBLE);
        getLocation();
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void errorToast() {
        Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

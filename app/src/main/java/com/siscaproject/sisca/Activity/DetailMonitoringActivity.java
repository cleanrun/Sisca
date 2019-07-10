package com.siscaproject.sisca.Activity;

import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Adapter.MonitoringDetailAdapter;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.User;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
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

public class DetailMonitoringActivity extends AppCompatActivity {

    @BindView(R.id.pv_report_detail)
    ProgressView progressView;
    @BindView(R.id.tv_title_report_detail)
    TextView tvTitle;
    @BindView(R.id.tv_date_report_detail)
    TextView tvDate;
    @BindView(R.id.tv_time_report_detail)
    TextView tvTime;
    @BindView(R.id.tv_pic_report_detail)
    TextView tvPic;
    @BindView(R.id.rv_report_detail)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty_report_detail)
    LinearLayout llEmpty;
    @BindView(R.id.btn_submit_report_detail)
    Button btnSubmit;

    /*List<AssetModel> assetList;
    List<MonitoringAssetModel> reportAssetList;*/
    private UserService userService;
    private List<AssetAPI> assetAPIList;
    private String idLocation;
    private boolean isFromReportActivity;
    private MonitoringDetailAdapter adapter;
    private int indexUpdate;
    private LocationAPI locationAPI;
    private ReportMonitoringActivity monitoringReportActivity;

    public void setMonitoringReportActivity(ReportMonitoringActivity monitoringReportActivity) {
        this.monitoringReportActivity = monitoringReportActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_detail);

        ButterKnife.bind(this);

        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        idLocation = getIntent().getIntExtra("ID_LOCATION_EXTRA", 0)+"";
        isFromReportActivity = getIntent().getBooleanExtra("FROM_REPORT_EXTRA", false);

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
                        }
                    }

                    assetAPIList.clear();
                    assetAPIList = tmpList;

                    showData();
                } else {
                    Log.i(Constraints.TAG, "onResponse: else");
                    errorToast();
                    showData();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetAPI>> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                errorToast();
                showData();
            }
        });
    }

    private void showData() {
        if (assetAPIList.isEmpty()){
            llEmpty.setVisibility(View.VISIBLE);
            btnSubmit.setBackground(getResources().getDrawable(R.drawable.shape_button_grey));
            btnSubmit.setEnabled(false);
        }
        else
            llEmpty.setVisibility(View.INVISIBLE);

        adapter = new MonitoringDetailAdapter(this, assetAPIList);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.cv_back_report_detail) void cvBackClick() {
        if (isFromReportActivity){
            //monitoringReportActivity.load();
            onBackPressed();
        }
        else{
            onBackPressed();
        }
    }

    @OnClick(R.id.btn_submit_report_detail) void btnSubmitClick() {
        progressView.setVisibility(View.VISIBLE);
        assetAPIList = adapter.getList();
        /*for (int i=0; i<assetAPIList.size(); i++){
            Log.d("astaga", assetAPIList.get(i).getCondition());
        }*/

        //Call<Asset> call = userService.storeFixed(auth, accept, asset);
        indexUpdate=0;
        updateData();

    }

    private void updateData() {
        String dateNow = Config.getDateNow();
        //Toast.makeText(this, dateNow, Toast.LENGTH_LONG).show();
        for (int i=0; i<assetAPIList.size(); i++){
            assetAPIList.get(i).setUpdated_at(dateNow);
        }

        if (indexUpdate<assetAPIList.size()){
            Call<AssetAPI> call = userService.putAsset(Header.auth, Header.accept, assetAPIList.get(indexUpdate).getId(), assetAPIList.get(indexUpdate));
            call.enqueue(new Callback<AssetAPI>() {
                @Override
                public void onResponse(Call<AssetAPI> call, Response<AssetAPI> response) {
                    if(response.isSuccessful()){
                        Log.d("hmm", "sukses "+indexUpdate);
                        indexUpdate++;
                        updateData();
                    }else{
                        try {
                            Log.d("hmm", "gagal "+indexUpdate);
                            Log.d("hmm", "gagal "+response.errorBody().string());
                            Toast.makeText(DetailMonitoringActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            //Log.e(TAG, "onResponse: exception");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssetAPI> call, Throwable throwable) {

                }
            });
        }
        else {
            locationAPI.setUpdated_at(dateNow);
            updateDataLocation();
        }
    }

    private void updateDataLocation() {
        Call<LocationAPI> call = userService.putLocation(Header.auth, Header.accept, locationAPI.getId(), locationAPI);
        call.enqueue(new Callback<LocationAPI>() {
            @Override
            public void onResponse(Call<LocationAPI> call, Response<LocationAPI> response) {
                if (response.isSuccessful()){
                    showToast("Data successfully inputted!");
                    Log.d("Finall", "sukses "+locationAPI.getUpdated_at());
                    //load ulang
                    assetAPIList.clear();
                    getLocation();
                }
                else{
                    Log.i(Constraints.TAG, "onResponse: else");
                    Log.d("Finall", "ga sukses "+locationAPI.getUpdated_at());
                    errorToast();
                    assetAPIList.clear();
                    getLocation();
                }
            }

            @Override
            public void onFailure(Call<LocationAPI> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                Log.d("Finall", "ga sukses "+locationAPI.getUpdated_at());
                errorToast();
                assetAPIList.clear();
                getLocation();
            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void errorToast() {
        Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

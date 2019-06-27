package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.Adapter.AssetAdapter;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.ResponseShow;
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

public class DetailLocationActivity extends AppCompatActivity {
    private static final String TAG = "DetailLocationActivity";

    @BindView(R.id.cv_back) CardView cvBack;
    @BindView(R.id.tv_title_location) TextView tvTitle;
    @BindView(R.id.tv_alamat) TextView tvAlamat;
    @BindView(R.id.tv_manager) TextView tvManager;
    @BindView(R.id.tv_jumlah_aset) TextView tvJumlahAset;
    @BindView(R.id.rv_list_aset) RecyclerView recyclerView;
    @BindView(R.id.btn_monitoring) AppCompatButton btnMonitoring;

    private UserService userService;

    private int ID_EXTRA;
    private LocationAPI data;

    private List<AssetMutasi> listAsset;

    private AssetAdapter.cardClickListener listener = new AssetAdapter.cardClickListener() {
        @Override
        public void onCardClick(int id) {
            // do nothing
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        try{
            ID_EXTRA = getIntent().getIntExtra("ID_EXTRA", 0);
            if(ID_EXTRA != 0){
                getData(ID_EXTRA);
            }else{
                Log.e(TAG, "onCreate else");
                showToast("Unable to get location data");
                finish();
            }
        }catch(Exception e){
            Log.e(TAG, "onCreate exception : " + e.getMessage() );
            showToast("Unable to get location data");
            finish();
        }
    }

    @OnClick({R.id.cv_back, R.id.btn_monitoring})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.cv_back:
                onBackPressed();
                break;
            case R.id.btn_monitoring:
                showToast("Monitoring");
                break;
            default:
                break;
        }
    }

    private void setFields(){
        tvTitle.setText(data.getName());
        tvAlamat.setText(data.getAddress());
        tvManager.setText(data.getManager().getName());
        tvJumlahAset.setText(String.valueOf(data.getAsset_count()));
    }

    private void setRecyclerView(List<AssetMutasi> list){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AssetAdapter adapter = new AssetAdapter(this, list, listener);
        recyclerView.setAdapter(adapter);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getData(int id){
        String idString = String.valueOf(id);
        Call<ResponseShow<LocationAPI>> call = userService.showLocation(idString, Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseShow<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseShow<LocationAPI>> call, Response<ResponseShow<LocationAPI>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "getData onResponse: successful");
                    data = response.body().getData();
                    setFields();

                    getAsset();
                }else{
                    Log.e(TAG, "getData onResponse: is not successful = " + response.message());
                    showToast("Failed to get asset data");
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseShow<LocationAPI>> call, Throwable t) {
                Log.e(TAG, "getData onFailure: " + t.getMessage() );
                showToast("Failed to get location data");
                //finish();
            }
        });

    }

    private void getAsset(){
        Call<ResponseIndex<AssetMutasi>> call = userService.indexAssetMutasi(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetMutasi>> call, Response<ResponseIndex<AssetMutasi>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "getAsset onResponse: total " + response.body().getTotal());
                    listAsset = response.body().getData();

                    Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                        @Override
                        public int compare(AssetMutasi assetAPI1, AssetMutasi assetAPI2) {
                            return assetAPI1.getName().compareTo(assetAPI2.getName());
                        }
                    });

                    List<AssetMutasi> tmpList = new ArrayList<>();
                    for(AssetMutasi a : listAsset){
                        if(a.getLocation_id() == ID_EXTRA){
                            tmpList.add(a);
                        }
                    }

                    listAsset.clear();
                    listAsset = tmpList;

                    setRecyclerView(listAsset);
                }else{
                    Log.e(TAG, "getAsset onResponse: not successful");
                    showToast("Unable to get asset list");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetMutasi>> call, Throwable t) {
                Log.e(TAG, "getAsset onFailure: " + t.getMessage() );
                showToast("Unable to get asset list");
            }
        });
    }

    private ArrayList<AssetMutasi> dummyData(){
        ArrayList<AssetMutasi> data = new ArrayList<>();

        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 1", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 2", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 3", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 4", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 5", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 6", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 7", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 8", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 9", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 10", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 11", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 12", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 13", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 14", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 15", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 16", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 17", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 18", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 19", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 20", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 21", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));

        return data;
    }
}

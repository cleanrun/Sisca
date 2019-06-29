package com.siscaproject.sisca.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Adapter.PindahLokasiAdapter;
import com.siscaproject.sisca.Adapter.PindahPicAdapter;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.ResponseShow;
import com.siscaproject.sisca.Model.ResponseStore;
import com.siscaproject.sisca.Model.User;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMutationActivity extends AppCompatActivity {

    private static final String TAG = "DetailMutationActivity";

    @BindView(R.id.pv_mutation_detail) ProgressView progressView;
    @BindView(R.id.cv_back_mutation_detail) CardView back;
    @BindView(R.id.iv_photo_mutation_detail) ImageView ivPhoto;
    @BindView(R.id.tv_name_mutation_detail) TextView tvName;
    @BindView(R.id.tv_id_mutation_detail) TextView tvId;
    @BindView(R.id.tv_desc_mutation_detail) TextView tvDesc;
    @BindView(R.id.et_location_before_mutation_detail) EditText etSebelumLokasi;
    @BindView(R.id.et_pindah_lokasi) EditText etPindahLokasi;
    @BindView(R.id.et_pic_before_mutation_detail) EditText etSebelumPic;
    @BindView(R.id.et_pindah_pic) EditText etPindahPic;
    @BindView(R.id.btn_pindah_lokasi) CardView btnPindahLokasi;
    @BindView(R.id.btn_pindah_pic) CardView btnPindahPic;
    @BindView(R.id.et_notes_mutation_detail) EditText etNotes;
    @BindView(R.id.btn_submit_mutation_detail) Button btnSubmit;

    private UserService userService;

    private AssetMutasi dataAsset;
    private ArrayList<LocationAPI> listLokasi;
    private ArrayList<User> listUser; // list PIC

    private int newLocation = 0;
    private int newPic = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mutation);

        userService = APIProperties.getUserService();
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID_ASSET_EXTRA", 0);

        getAssetData(id);
        getLokasi();

        ButterKnife.bind(this);

        progressView.setVisibility(View.GONE);
    }

    private void setBeforeFields(){
        tvName.setText(dataAsset.getName());
        tvId.setText(dataAsset.getAsset_id());
        tvDesc.setText(dataAsset.getDescription());
        //etSebelumLokasi.setText(String.valueOf(dataAsset.getLocation_id()));
        //etSebelumPic.setText(String.valueOf(dataAsset.getPic_id()));
        etSebelumLokasi.setText(dataAsset.getLocation().getName());
        etSebelumPic.setText(dataAsset.getPic().getName());
    }

    @OnClick(R.id.cv_back_mutation_detail)
    public void back(View view){
        if(view.getId() == R.id.cv_back_mutation_detail){
            onBackPressed();
        }
    }

    @OnClick(R.id.btn_pindah_lokasi)
    public void dialogPindahLokasi(View view){
        if(view.getId() == R.id.btn_pindah_lokasi){
            showPindahLokasiDialog();
        }
    }

    @OnClick(R.id.btn_pindah_pic)
    public void dialogPindahPic(View view){
        if(view.getId() == R.id.btn_pindah_pic){
            showPindahPicDialog();
        }
    }

    @OnClick(R.id.btn_submit_mutation_detail)
    public void submit(View view){
        Toast.makeText(this, "Under Maintenance :)", Toast.LENGTH_SHORT).show();
        //submitMutasi();
    }

    private void setNewLocation(int id){
        newLocation = id;
    }

    private void setNewPic(int id){
        newPic = id;
    }

    private ArrayList<String> dataLokasi(){ // data lokal
        ArrayList<String> data = new ArrayList<>();
        data.add("C102");
        data.add("C103");
        data.add("C201");
        data.add("C202");
        data.add("B101");
        data.add("B102");

        return data;
    }

    private ArrayList<String> dataPic(){ // data lokal
        ArrayList<String> data = new ArrayList<>();
        data.add("Riski Novanda");
        data.add("Susi Susanto");
        data.add("Abdul Kadir");
        data.add("Roberto Karlos");
        data.add("Ivan Gunawan");
        data.add("Imam Agus Faisal");
        data.add("Muhammad Idam");

        return data;
    }

    private void showPindahLokasiDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pindah_lokasi);
        dialog.setCancelable(true);

        PindahLokasiAdapter.OnItemClickListener listener = new PindahLokasiAdapter.OnItemClickListener() {
            @Override
            public void putLokasi(final int id, String title) {
                //etPindahLokasi.setText(String.valueOf(id));
                etPindahLokasi.setText(title);
                setNewLocation(id);
                Log.i(TAG, "new location id : " + newLocation);
                dialog.dismiss();
            }
        };

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btnBatal = dialog.findViewById(R.id.btn_batal);
        final Button btnPilih = dialog.findViewById(R.id.btn_pilih);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_list_lokasi);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //PindahLokasiAdapter adapter = new PindahLokasiAdapter(dataLokasi(), getApplicationContext(), listener);
        PindahLokasiAdapter adapter = new PindahLokasiAdapter(listLokasi, getApplicationContext(), listener);
        recyclerView.setAdapter(adapter);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showPindahPicDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pindah_pic);
        dialog.setCancelable(true);

        PindahPicAdapter.OnItemClickListener listener = new PindahPicAdapter.OnItemClickListener() {
            @Override
            public void putPic(String pic) {
                etPindahPic.setText(pic);
                dialog.dismiss();
            }
        };

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btnBatal = dialog.findViewById(R.id.btn_batal);
        final Button btnPilih = dialog.findViewById(R.id.btn_pilih);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_list_pic);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PindahPicAdapter adapter = new PindahPicAdapter(dataPic(), getApplicationContext(), listener);
        recyclerView.setAdapter(adapter);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getAssetData(final int id){
        String idString = String.valueOf(id);

        Call<ResponseShow<AssetMutasi>> call = userService.showAset(idString, Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseShow<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseShow<AssetMutasi>> call, Response<ResponseShow<AssetMutasi>> response) {
                if(response.isSuccessful()){
                    //String name = response.body().getData().getName();
                    dataAsset = response.body().getData();
                    setBeforeFields();
                    Log.i(TAG, "getAssetData onResponse: successful");
                    Log.i(TAG, "getAssetData onResponse: asset name == " + dataAsset.getName());
                    Log.i(TAG, "getAssetData onResponse: asset location == " + dataAsset.getLocation().getName());
                    Log.i(TAG, "getAssetData onResponse: asset pic == " + dataAsset.getPic().getName());
                }
                else{
                    Log.e(TAG, "getAssetData onResponse: is not successful");
                    showToast("Gagal mengambil data asset");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseShow<AssetMutasi>> call, Throwable t) {
                Log.e(TAG, "getAssetData onFailure: message == " + t.getMessage() );
                showToast("Gagal mengambil data asset");
                finish();
            }
        });
    }

    private void getLokasi(){
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "getLokasi onResponse: successful");
                    ArrayList<LocationAPI> data = response.body().getData();
                    listLokasi = data;
                } else {
                    Log.e(TAG, "getLokasi onResponse: not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable t) {
                Log.e(TAG, "getLokasi onFailure: " + t.getMessage());
            }
        });
    }

    private void submitMutasi(int asset_id, String description, int new_location_id, int new_pic_id, String reason){
        Call<ResponseStore> call = userService.storeMutation(Header.auth, Header.accept, asset_id,
                description, new_location_id, new_pic_id, reason);
        call.enqueue(new Callback<ResponseStore>() {
            @Override
            public void onResponse(Call<ResponseStore> call, Response<ResponseStore> response) {
                // TODO: Finish this (submitMutasi())
                if(response.isSuccessful()){
                    Log.i(TAG, "submitMutasi onResponse: message " + response.body().getMessage());
                    Log.i(TAG, "submitMutasi onResponse: status " + response.body().getStatus());
                    showToast("Mutasi berhasil");
                    //finish();
                }else{
                    Log.e(TAG, "submitMutasi onResponse: not successful ");
                    showToast("Gagal melakukan mutasi data");
                }
            }

            @Override
            public void onFailure(Call<ResponseStore> call, Throwable t) {
                Log.e(TAG, "submitMutasi onFailure: " + t.getMessage() );
                showToast("Gagal melakukan mutasi data");
            }
        });
    }

}

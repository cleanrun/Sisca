package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Model.AssetModel;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.ResponseShow;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.DummyData;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// HomeAsetDetailActivity

public class DetailAssetActivity extends AppCompatActivity {
    private static final String TAG = "DetailAssetActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.cv_option_home_aset_detail) CardView cvMenu;
    @BindView(R.id.pv_home_aset_detail) ProgressView progressView;
    @BindView(R.id.tv_name_home_aset_detail) TextView tvName;
    @BindView(R.id.tv_id_home_aset_detail) TextView tvIdAset;
    @BindView(R.id.tv_lokasi_home_aset_detail) TextView tvIdLokasi;
    @BindView(R.id.tv_pic_home_aset_detail) TextView tvPic;
    @BindView(R.id.tv_desc_home_aset_detail) TextView tvDesc;
    @BindView(R.id.tv_bulan) TextView tvBulan;
    @BindView(R.id.tv_tgl_beli) TextView tvTglBeli;
    @BindView(R.id.tv_harga) TextView tvHarga;
    @BindView(R.id.tv_value) TextView tvValue;
    @BindView(R.id.tv_depresiasi) TextView tvDepresiasi;
    @BindView(R.id.iv_image_aset_detail)
    ImageView ivImage;

    private MenuItem monitoringMenu;
    private MenuItem mutationMenu;

    private UserService userService;

    private List<AssetModel> assetModelList;

    private int ID_EXTRA;
    private AssetMutasi data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aset);

        ButterKnife.bind(this);
        userService = APIProperties.getUserService();

        try{
            ID_EXTRA = getIntent().getIntExtra("ID_ASSET_EXTRA", 0);
            if(ID_EXTRA != 0){
                getData(ID_EXTRA);
            }else{
                Log.e(TAG, "onCreate else");
                showToast("Unable to get Asset Data");
                finish();
            }
        }catch(Exception e){
            Log.e(TAG, "onCreate exception : " + e.getMessage() );
            showToast("Unable to get Asset Data");
            finish();
        }

    }

    @OnClick(R.id.cv_back_home_aset_detail) void cvBackOnClick(){
        onBackPressed();
    }

    @OnClick(R.id.cv_option_home_aset_detail) void cvMenuOnClick(){
        PopupMenu pm = new PopupMenu(this, cvMenu);
        pm.getMenuInflater().inflate(R.menu.menu_detail_aset, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.menu_monitoring:
                        showToast("Monitoring");
                        break;
                    case R.id.menu_mutation:
                        //showToast("Mutation");
                        Intent mutationIntent = new Intent(DetailAssetActivity.this, DetailMutationActivity.class);
                        mutationIntent.putExtra("ID_ASSET_EXTRA", ID_EXTRA);
                        DetailAssetActivity.this.startActivity(mutationIntent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        pm.show();
    }

    private void setFields(){
        tvName.setText(data.getName());
        tvIdAset.setText(data.getAsset_id());
        tvIdLokasi.setText(data.getLocation().getName());
        tvPic.setText(data.getPic().getName());
        tvDesc.setText(data.getDescription());
        //tvBulan.setText();
        tvTglBeli.setText(data.getDate_purchase());
        tvHarga.setText(data.getPrice());
        //tvValue.setText();
        //tvDepresiasi.setText();
        if (data.getImage()==null)
            Picasso.get().load(R.drawable.image_null).into(ivImage);
        else
            Picasso.get().load(data.getImage()).into(ivImage);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getData(int id){
        String idString = String.valueOf(id);
        Call<ResponseShow<AssetMutasi>> call = userService.showAset(idString, Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseShow<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseShow<AssetMutasi>> call, Response<ResponseShow<AssetMutasi>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: successful");
                    data = response.body().getData();
                    setFields();
                }else{
                    Log.e(TAG, "onFailure: is not successful = " + response.message());
                    showToast("Failed to get asset data");
                }
            }

            @Override
            public void onFailure(Call<ResponseShow<AssetMutasi>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
                showToast("Failed to get asset data");
                //finish();
            }
        });
    }
}

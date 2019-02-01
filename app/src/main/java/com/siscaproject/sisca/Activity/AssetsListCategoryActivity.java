package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Adapter.AssetsListCategoryAdapter;
import com.siscaproject.sisca.Model.AssetStock;
import com.siscaproject.sisca.Model.ResponseAssetStock;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetsListCategoryActivity extends AppCompatActivity {
    private static final String TAG = "AssetsListCategory";

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.btn_create_new) LinearLayout btn_create_new;
    @BindView(R.id.rv_list_asset) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private AssetsListCategoryAdapter adapter;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_list_category);
        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title", "Assets");
        tv_title.setText(title);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Prefs.Builder()
                .setContext(this)
                .setMode(android.content.ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @OnClick(R.id.btn_create_new)
    public void onClick(View view){
        if(view.getId() == R.id.btn_create_new){
            try{
                //getAsset();
                Intent intent = new Intent(AssetsListCategoryActivity.this, FormNewAssetActivity.class);
                //intent.putExtra("asset_tag", "1234567");
                startActivity(intent);
            }catch(Exception e){
                Log.e(TAG, "onClick: exception = " + e.getMessage() );
            }
        }
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // On Progress
    private void getAsset(){
        String auth = Prefs.getString("token_type", "null")
                + " " + Prefs.getString("access_token", "null");
        String accept = "application/json";

        showProgressBar();

        Call<ResponseAssetStock> call = userService.indexStock(auth, accept);
        call.enqueue(new Callback<ResponseAssetStock>() {
            @Override
            public void onResponse(Call<ResponseAssetStock> call, Response<ResponseAssetStock> response) {
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);
                }
                else{
                    Log.i(TAG, "onResponse: else isSuccessful");
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ResponseAssetStock> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

}

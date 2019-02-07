package com.siscaproject.sisca.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Adapter.ModelAssetsAdapter;
import com.siscaproject.sisca.Model.Model;
import com.siscaproject.sisca.Model.ResponseModel;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelAssetsActivity extends AppCompatActivity {
    private static final String TAG = "ModelAssetsActivity";

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_assets_model) RecyclerView recyclerView;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private ModelAssetsAdapter adapter;
    private UserService userService;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_assets);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getModel();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getModel();
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onClick(View view){
        if(view.getId() == R.id.fab_add){
            Toast.makeText(this, "Add Asset Model", Toast.LENGTH_SHORT).show();
        }
    }

    private void getModel(){
        refresh.setRefreshing(true);

        Call<ResponseModel> call = userService.indexModel(auth, accept);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse model: model total " + response.body().getTotal());
                    ArrayList<Model> listModel = response.body().getRows();

                    adapter = new ModelAssetsAdapter(listModel, ModelAssetsActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse model: else");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure model: " + t.getMessage() );
                Toast.makeText(ModelAssetsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

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
import com.siscaproject.sisca.Adapter.ManufacturerAdapter;
import com.siscaproject.sisca.Model.Manufacturer;
import com.siscaproject.sisca.Model.ResponseIndex;
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

public class ManufacturerActivity extends AppCompatActivity {
    private static final String TAG = "ManufacturerActivity";

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.rv_list_manufacturer) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private ManufacturerAdapter adapter;
    private UserService userService;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getManufacturer();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getManufacturer();
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onClick(View view){
        if(view.getId() == R.id.fab_add){
            Toast.makeText(this, "Add Manufacturer", Toast.LENGTH_SHORT).show();
        }
    }

    private void getManufacturer(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Manufacturer>> call = userService.indexManufacturer(auth, accept);
        call.enqueue(new Callback<ResponseIndex<Manufacturer>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Manufacturer>> call, Response<ResponseIndex<Manufacturer>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: manufacturer total " + response.body().getTotal());

                    ArrayList<Manufacturer> listData = response.body().getRows();

                    adapter = new ManufacturerAdapter(listData, ManufacturerActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: " + response.errorBody().string() );
                        errorToast();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch" + e.getMessage() );
                        errorToast();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Manufacturer>> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorToast();
            }
        });
    }

    private void errorToast(){
        Toast.makeText(ManufacturerActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

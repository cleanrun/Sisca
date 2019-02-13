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
import com.siscaproject.sisca.Adapter.SupplierAdapter;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.Supplier;
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

public class SupplierActivity extends AppCompatActivity {
    private static final String TAG = "SupplierActivity";

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.rv_list_supplier) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private SupplierAdapter adapter;
    private UserService userService;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupplier();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupplier();
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onClick(View view){
        if(view.getId() == R.id.fab_add){
            Toast.makeText(this, "Add Supplier", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSupplier(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Supplier>> call = userService.indexSupplier(auth, accept);
        call.enqueue(new Callback<ResponseIndex<Supplier>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Supplier>> call, Response<ResponseIndex<Supplier>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: supplier total " + response.body().getTotal());

                    ArrayList<Supplier> listData = response.body().getRows();

                    adapter = new SupplierAdapter(listData, SupplierActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse: else" + response.errorBody());
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Supplier>> call, Throwable t) {
                refresh.setRefreshing(false);

                Log.e(TAG, "onFailure: supplier" + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast(){
        Toast.makeText(SupplierActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

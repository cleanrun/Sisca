package com.siscaproject.sisca.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.ActivityForm.FormNewManufacturerActivity;
import com.siscaproject.sisca.Adapter.ManufacturerAdapter;
import com.siscaproject.sisca.Model.Manufacturer;
import com.siscaproject.sisca.Model.ResponseIndex;
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

public class ManufacturerActivity extends AppCompatActivity {
    private static final String TAG = "ManufacturerActivity";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.btn_reader) ImageButton btnReader;
    @BindView(R.id.btn_add) ImageButton btnAdd;
    @BindView(R.id.rv_list_manufacturer) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;

    private ManufacturerAdapter adapter;
    private UserService userService;

    private ManufacturerAdapter.OnButtonClickListener listener = new ManufacturerAdapter.OnButtonClickListener() {
        @Override
        public void showDetails(Manufacturer manufacturer) {
            //Toast.makeText(ManufacturerActivity.this, "Details", Toast.LENGTH_SHORT).show();
            showDetailsDialog(manufacturer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        getManufacturer();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getManufacturer();
            }
        });
    }

    @OnClick({R.id.btn_reader, R.id.btn_add})
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.btn_reader:
                try{
                    startActivity(new Intent(this, BluetoothActivity.class));
                }catch(Exception e){
                    errorToast();
                }
                break;
            case R.id.btn_add:
                try{
                    startActivity(new Intent(this, FormNewManufacturerActivity.class));
                }catch(Exception e){
                    errorToast();
                }
                break;
        }
    }

    private void getManufacturer(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Manufacturer>> call = userService.indexManufacturer(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Manufacturer>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Manufacturer>> call, Response<ResponseIndex<Manufacturer>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: manufacturer total " + response.body().getTotal());

                    ArrayList<Manufacturer> listData = response.body().getRows();

                    adapter = new ManufacturerAdapter(listData, ManufacturerActivity.this, listener);
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

    private void showDetailsDialog(Manufacturer manufacturer){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_detail_manufacturer);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        final TextView tvName = dialog.findViewById(R.id.tv_name);
        final AppCompatEditText etUrl = dialog.findViewById(R.id.et_url);
        final AppCompatEditText etSupportUrl = dialog.findViewById(R.id.et_support_url);
        final AppCompatEditText etSupportPhone = dialog.findViewById(R.id.et_support_phone);
        final AppCompatEditText etSupportEmail = dialog.findViewById(R.id.et_support_email);

        tvName.setText(manufacturer.getName());
        etUrl.setText(manufacturer.getUrl());
        etSupportUrl.setText(manufacturer.getSupport_url());
        etSupportPhone.setText(manufacturer.getSupport_phone());
        etSupportEmail.setText(manufacturer.getSupport_email());

        etUrl.setKeyListener(null);
        etSupportUrl.setKeyListener(null);
        etSupportPhone.setKeyListener(null);
        etSupportEmail.setKeyListener(null);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void errorToast(){
        Toast.makeText(ManufacturerActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

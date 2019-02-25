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

import com.siscaproject.sisca.ActivityForm.FormNewSupplierActivity;
import com.siscaproject.sisca.Adapter.SupplierAdapter;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.Supplier;
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

public class SupplierActivity extends AppCompatActivity {
    private static final String TAG = "SupplierActivity";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.rv_list_supplier) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private SupplierAdapter adapter;
    private UserService userService;

    private SupplierAdapter.OnButtonClickListener listener = new SupplierAdapter.OnButtonClickListener() {
        @Override
        public void showDetails(Supplier supplier) {
            showDetailsDialog(supplier);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
            //Toast.makeText(this, "Add Supplier", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FormNewSupplierActivity.class));
        }
    }

    private void getSupplier(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Supplier>> call = userService.indexSupplier(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Supplier>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Supplier>> call, Response<ResponseIndex<Supplier>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: supplier total " + response.body().getTotal());

                    ArrayList<Supplier> listData = response.body().getRows();

                    adapter = new SupplierAdapter(listData, SupplierActivity.this, listener);
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

    private void showDetailsDialog(Supplier supplier){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_detail_supplier);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        final TextView tvName = dialog.findViewById(R.id.tv_name);
        final AppCompatEditText etCity = dialog.findViewById(R.id.et_city);
        final AppCompatEditText etState = dialog.findViewById(R.id.et_state);
        final AppCompatEditText etCountry = dialog.findViewById(R.id.et_country);
        final AppCompatEditText etAddress = dialog.findViewById(R.id.et_address);
        final AppCompatEditText etPhone = dialog.findViewById(R.id.et_phone);
        final AppCompatEditText etEmail = dialog.findViewById(R.id.et_email);
        final AppCompatEditText etZip = dialog.findViewById(R.id.et_zip);
        final AppCompatEditText etUrl = dialog.findViewById(R.id.et_url);

        tvName.setText(supplier.getName());
        etCity.setText(supplier.getCity());
        etState.setText(supplier.getState());
        etCountry.setText(supplier.getCountry());
        etAddress.setText(supplier.getAddress());
        etPhone.setText(supplier.getPhone());
        etEmail.setText(supplier.getEmail());
        etZip.setText(supplier.getZip());
        etUrl.setText(supplier.getUrl());

        etCity.setKeyListener(null);
        etState.setKeyListener(null);
        etCountry.setKeyListener(null);
        etAddress.setKeyListener(null);
        etPhone.setKeyListener(null);
        etEmail.setKeyListener(null);
        etZip.setKeyListener(null);
        etUrl.setKeyListener(null);

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
        Toast.makeText(SupplierActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

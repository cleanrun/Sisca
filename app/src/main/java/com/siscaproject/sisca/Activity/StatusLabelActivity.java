package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.ActivityForm.FormNewStatusLabelActivity;
import com.siscaproject.sisca.Adapter.StatusLabelAdapter;
import com.siscaproject.sisca.Model.Label;
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

public class StatusLabelActivity extends AppCompatActivity {
    private static final String TAG = "StatusLabelActivity";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.btn_reader) ImageButton btnReader;
    @BindView(R.id.btn_add) ImageButton btnAdd;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_status_label) RecyclerView recyclerView;

    private StatusLabelAdapter adapter;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_label);

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

        getLabel();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLabel();
            }
        });

        new Prefs.Builder()
                .setContext(this)
                .setMode(android.content.ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
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
                    startActivity(new Intent(this, FormNewStatusLabelActivity.class));
                }catch(Exception e){
                    errorToast();
                }
                break;
        }
    }

    private void getLabel(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Label>> call = userService.indexLabel(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Label>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Label>> call, Response<ResponseIndex<Label>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse label: label total " + response.body().getTotal());
                    ArrayList<Label> listLabel = response.body().getRows();

                    adapter = new StatusLabelAdapter(listLabel, StatusLabelActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse label: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Label>> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure label: " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast(){
        Toast.makeText(StatusLabelActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

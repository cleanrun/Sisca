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
import com.siscaproject.sisca.Adapter.StatusLabelAdapter;
import com.siscaproject.sisca.Model.Label;
import com.siscaproject.sisca.Model.ResponseLabel;
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

public class StatusLabelActivity extends AppCompatActivity {
    private static final String TAG = "StatusLabelActivity";

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_status_label) RecyclerView recyclerView;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private StatusLabelAdapter adapter;
    private UserService userService;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_label);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    @OnClick(R.id.fab_add)
    public void onClick(View view){
        if(view.getId() == R.id.fab_add){
            Toast.makeText(this, "Add Status Label", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLabel(){
        refresh.setRefreshing(true);

        Call<ResponseLabel> call = userService.indexLabel(auth, accept);
        call.enqueue(new Callback<ResponseLabel>() {
            @Override
            public void onResponse(Call<ResponseLabel> call, Response<ResponseLabel> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse label: label total " + response.body().getTotal());
                    ArrayList<Label> listLabel = response.body().getRows();

                    adapter = new StatusLabelAdapter(listLabel, StatusLabelActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse label: else");
                }
            }

            @Override
            public void onFailure(Call<ResponseLabel> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure label: " + t.getMessage() );
                Toast.makeText(StatusLabelActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

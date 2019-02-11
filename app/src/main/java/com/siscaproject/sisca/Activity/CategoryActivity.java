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
import com.siscaproject.sisca.Adapter.CategoryAdapter;
import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.Model.ResponseCategory;
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

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "CategoryActivity";

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.rv_list_category) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private CategoryAdapter adapter;
    private UserService userService;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCategory();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategory();
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onClick(View view){
        if(view.getId() == R.id.fab_add){
            Toast.makeText(this, "Add Category", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategory(){
        refresh.setRefreshing(true);

        Call<ResponseCategory> call = userService.indexCategory(auth, accept);
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                refresh.setRefreshing(true);
                
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse category: total " + response.body().getTotal());

                    ArrayList<Category> listData = response.body().getRows();

                    adapter = new CategoryAdapter(listData, CategoryActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse label: else");
                    Toast.makeText(CategoryActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure label: " + t.getMessage() );
                Toast.makeText(CategoryActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

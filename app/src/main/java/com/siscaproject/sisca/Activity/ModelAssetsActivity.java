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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.ActivityForm.FormNewAssetsModelActivity;
import com.siscaproject.sisca.Adapter.ModelAssetsAdapter;
import com.siscaproject.sisca.Model.Model;
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

public class ModelAssetsActivity extends AppCompatActivity {
    private static final String TAG = "ModelAssetsActivity";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_assets_model) RecyclerView recyclerView;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private ModelAssetsAdapter adapter;
    private UserService userService;

    private ModelAssetsAdapter.OnButtonClickListener listener = new ModelAssetsAdapter.OnButtonClickListener() {
        @Override
        public void showDetails(Model model) {
            showDetailsDialog(model);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_assets);

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
            //Toast.makeText(this, "Add Asset Model", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ModelAssetsActivity.this, FormNewAssetsModelActivity.class));
        }
    }

    private void getModel(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Model>> call = userService.indexModel(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Model>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Model>> call, Response<ResponseIndex<Model>> response) {
                refresh.setRefreshing(false);

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse model: model total " + response.body().getTotal());
                    ArrayList<Model> listModel = response.body().getRows();

                    adapter = new ModelAssetsAdapter(listModel, ModelAssetsActivity.this, listener);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse model: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Model>> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure model: " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void showDetailsDialog(Model model){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_detail_model);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        final ImageView ivPhoto = dialog.findViewById(R.id.iv_photo);
        final TextView tvName = dialog.findViewById(R.id.tv_name);
        final AppCompatEditText etModelNumber = dialog.findViewById(R.id.et_model_number);
        final AppCompatEditText etNotes = dialog.findViewById(R.id.et_notes);

        // TODO: complete the attributes of the model class

        tvName.setText(model.getName());
        etModelNumber.setText(model.getModel_number());
        etNotes.setText(model.getNotes());

        etModelNumber.setKeyListener(null);
        etNotes.setKeyListener(null);

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
        Toast.makeText(ModelAssetsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}

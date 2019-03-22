package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.ActivityForm.FormNewAssetActivity;
import com.siscaproject.sisca.Adapter.AssetsAdapter;
import com.siscaproject.sisca.Fragment.EditAssetFragment;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.ResponseDelete;
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

public class AssetsActivity extends AppCompatActivity implements EditAssetFragment.OnFragmentInteractionListener{
    private static final String TAG = "AssetsListCategory";

    public static final int DIALOG_QUEST_CODE = 300;

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.btn_reader) ImageButton btnReader;
    @BindView(R.id.btn_add) ImageButton btnAdd;
    @BindView(R.id.rv_list_asset) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;

    private MaterialDialog createDialog;
    private MaterialDialog deleteDialog;
    private MaterialDialog loadingDialog;

    private AssetsAdapter adapter;

    private UserService userService;

    private AssetsAdapter.OnButtonClickListener listener = new AssetsAdapter.OnButtonClickListener() {
        @Override
        public void showDeleteDialog(final int id) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(AssetsActivity.this)
                    .content("Are you sure you want to delete this data?")
                    .contentGravity(GravityEnum.CENTER)
                    .autoDismiss(true)
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteAsset(id);
                            //Log.i(TAG, "onClick: ID deleted == " + id);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // do nothing
                        }
                    })
                    .canceledOnTouchOutside(true);

            deleteDialog = builder.build();
            deleteDialog.show();
        }

        @Override
        public void showEditDialog(Asset asset) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            EditAssetFragment fragment = new EditAssetFragment();
            fragment.setAsset(asset);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);
        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title", "Assets");
        //tv_title.setText(title);

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

        getAsset();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAsset();
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
                    //startActivity(new Intent(AssetsActivity.this, BluetoothActivity.class));
                    showReaderDialog();
                }catch(Exception e){
                    errorToast();
                }
                break;
            case R.id.btn_add:
                try{
                    startActivity(new Intent(AssetsActivity.this, FormNewAssetActivity.class));
                }catch(Exception e){
                    errorToast();
                }
                break;
        }
    }

    private void showReaderDialog() {
        Log.i(TAG, "showCreateDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(AssetsActivity.this)
                .content("Please select reader")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Bluetooth Reader")
                .negativeText("QR/Barcode Reader")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(AssetsActivity.this, BluetoothActivity.class));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(AssetsActivity.this, QRActivity.class));
                    }
                })
                .canceledOnTouchOutside(true);

        createDialog = builder.build();
        createDialog.show();
    }

    private void showLoadingDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Deleting data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loadingDialog = builder.build();
        loadingDialog.show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // On Progress
    private void getAsset() {
        showProgressBar();
        Call<ResponseIndex<Asset>> call = userService.indexFixed(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Asset>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Asset>> call, Response<ResponseIndex<Asset>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);

                    ArrayList<Asset> rows = response.body().getRows();
                    adapter = new AssetsAdapter(rows, getApplicationContext(), userService, listener);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                }
                hideProgressBar();
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseIndex<Asset>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorToast();
            }
        });
    }

    private void deleteAsset(int id){
        showLoadingDialog();

        Call<ResponseDelete> call = userService.deleteFixed(Header.auth, Header.accept, id);
        call.enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: delete asset " + response.body());
                    deleteToast();
                    getAsset();
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: delete asset" + response.errorBody().string() );
                        errorToast();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseDelete> call, Throwable t) {
                loadingDialog.dismiss();

                Log.e(TAG, "onFailure: delete asset" + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast() {
        Toast.makeText(AssetsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void deleteToast(){
        Toast.makeText(AssetsActivity.this, "Data deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAsset();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Still null
    }
}

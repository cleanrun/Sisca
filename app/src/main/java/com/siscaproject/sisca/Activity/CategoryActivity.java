package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.ActivityForm.FormNewCategoryActivity;
import com.siscaproject.sisca.Adapter.CategoryAdapter;
import com.siscaproject.sisca.Fragment.EditAssetFragment;
import com.siscaproject.sisca.Fragment.EditCategoryFragment;
import com.siscaproject.sisca.Model.Category;
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

public class CategoryActivity extends AppCompatActivity implements EditCategoryFragment.OnFragmentInteractionListener{
    private static final String TAG = "CategoryActivity";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.rv_list_category) RecyclerView recyclerView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    private MaterialDialog deleteDialog;
    private MaterialDialog loadingDialog;

    private CategoryAdapter adapter;
    private UserService userService;

    private CategoryAdapter.OnButtonClickListener listener = new CategoryAdapter.OnButtonClickListener() {
        @Override
        public void showDeleteDialog(final int id) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(CategoryActivity.this)
                    .content("Are you sure you want to delete this data?")
                    .contentGravity(GravityEnum.CENTER)
                    .autoDismiss(true)
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteCategory(id);
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
        public void showEditDialog(Category category) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            EditCategoryFragment fragment = new EditCategoryFragment();
            fragment.setCategory(category);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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
            //Toast.makeText(this, "Add Category", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FormNewCategoryActivity.class));
        }
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

    private void getCategory(){
        refresh.setRefreshing(true);

        Call<ResponseIndex<Category>> call = userService.indexCategory(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Category>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Category>> call, Response<ResponseIndex<Category>> response) {
                refresh.setRefreshing(false);
                
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse category: total " + response.body().getTotal());

                    ArrayList<Category> listData = response.body().getRows();

                    adapter = new CategoryAdapter(listData, CategoryActivity.this, listener);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.e(TAG, "onResponse label: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Category>> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure category: " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void deleteCategory(int id){
        showLoadingDialog();

        Call<ResponseDelete> call = userService.deleteCategory(Header.auth, Header.accept, id);
        call.enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: delete category " + response.body());
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: delete category" + response.errorBody().string() );
                        errorToast();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDelete> call, Throwable t) {
                loadingDialog.dismiss();

                Log.e(TAG, "onFailure: delete category " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast(){
        Toast.makeText(CategoryActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void deleteToast(){
        Toast.makeText(CategoryActivity.this, "Data deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do nothing
    }
}

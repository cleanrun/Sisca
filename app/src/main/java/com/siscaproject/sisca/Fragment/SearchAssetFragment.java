package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.ActivityForm.FormNewAssetActivity;
import com.siscaproject.sisca.Adapter.AssetsAdapter;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAssetFragment extends Fragment {
    private static final String TAG = "SearchAssetFragment";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_asset) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    //@BindView(R.id.fab_add) FloatingActionButton fab_add;

    private MaterialDialog createDialog;

    private AssetsAdapter adapter;

    private ArrayList<Asset> listData = new ArrayList<>();

    private UserService userService;

    private OnFragmentInteractionListener mListener;

    private AssetsAdapter.OnButtonClickListener listener = new AssetsAdapter.OnButtonClickListener() {
        @Override
        public void showDeleteDialog(int id) {
            // TODO: Complete showDeleteDialog()
        }

        @Override
        public void showEditDialog(Asset asset) {
            // TODO: Complete showEditDialog()
        }
    };

    public SearchAssetFragment() {
        // Required empty public constructor
    }

    public static SearchAssetFragment newInstance() {
        SearchAssetFragment fragment = new SearchAssetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_asset, container, false);

        ButterKnife.bind(this, view);
        userService = APIProperties.getUserService();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //adapter = new AssetsAdapter(listData, getActivity(), userService, listener);
        //recyclerView.setAdapter(adapter);

        getAsset();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAsset();
            }
        });


        return view;
    }

    private void showCreateDialog(){
        Log.i(TAG, "showCreateDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .content("Please select create method.")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Bluetooth Reader")
                .negativeText("Asset Form")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getContext(), BluetoothActivity.class));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getContext(), FormNewAssetActivity.class));
                    }
                })
                .canceledOnTouchOutside(true);

        createDialog = builder.build();
        createDialog.show();
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // On Progress
    private void getAsset(){
        showProgressBar();
        Call<ResponseIndex<Asset>> call = userService.indexFixed(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Asset>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Asset>> call, Response<ResponseIndex<Asset>> response) {
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);

                    ArrayList<Asset> rows = response.body().getRows();
                    adapter = new AssetsAdapter(rows, getActivity(), userService, listener);
                    recyclerView.setAdapter(adapter);

                }
                else{
                    Log.i(TAG, "onResponse: else");
                }
                hideProgressBar();
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseIndex<Asset>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

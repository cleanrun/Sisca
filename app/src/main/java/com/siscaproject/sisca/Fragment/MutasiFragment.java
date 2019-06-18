package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.Activity.DetailMutationActivity;
import com.siscaproject.sisca.Activity.QRActivity;
import com.siscaproject.sisca.Activity.SearchActivity;
import com.siscaproject.sisca.Adapter.AssetAdapter;
import com.siscaproject.sisca.Model.AssetMutasi;
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

public class MutasiFragment extends Fragment {
    private static final String TAG = "MutasiFragment";

    private OnFragmentInteractionListener mListener;

    private ArrayList<AssetMutasi> listAsset; // List terakhir mutasi

    @BindView(R.id.cv_scanner) CardView cvScanner;
    @BindView(R.id.cv_search) CardView cvSearch;
    @BindView(R.id.btn_filter) AppCompatImageButton btnFilter;
    @BindView(R.id.rv_mutasi_terakhir) RecyclerView recyclerView;
    @BindView(R.id.pv_mutasi) ProgressView progressView;

    private UserService userService;

    private MaterialDialog scannerDialog;

    private AssetAdapter.cardClickListener clickListener = new AssetAdapter.cardClickListener() {
        @Override
        public void onCardClick(final int id) {
            Intent intent = new Intent(getActivity(), DetailMutationActivity.class);
            intent.putExtra("assetID", id);
            startActivity(intent);
        }
    };

    public MutasiFragment() {
        // Required empty public constructor
    }

    public static MutasiFragment newInstance() {
        MutasiFragment fragment = new MutasiFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mutasi, container, false);
        ButterKnife.bind(this, view);

        userService = APIProperties.getUserService();
        getDataAsset();

        return view;
    }

    @OnClick({R.id.cv_scanner, R.id.cv_search, R.id.btn_filter})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.cv_scanner:
                //showToast("Scanner");
                showScannerDialog();
                break;
            case R.id.cv_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                searchIntent.putExtra("EXTRA_SEARCH", "");
                startActivity(searchIntent);
                break;
            case R.id.btn_filter:
                showToast("Filter");
                break;
            default:
                break;
        }
    }

    public void getDataAsset(){
        progressView.setVisibility(View.VISIBLE);

        Call<ResponseIndex<AssetMutasi>> call = userService.indexAssetMutasi(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetMutasi>> call, Response<ResponseIndex<AssetMutasi>> response) {
                progressView.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    Log.d(TAG, "onResponse asset: total asset " + total);

                    listAsset = response.body().getData();

                    showData();
                }
                else{
                    Log.e(TAG, "onFailure: not successful");
                    showToast("Unable to fetch the data.");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetMutasi>> call, Throwable t) {
                progressView.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
                showToast("Unable to fetch the data.");
            }
        });
    }

    private void showData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        AssetAdapter adapter = new AssetAdapter(getContext(), listAsset, clickListener);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showScannerDialog() {
        Log.i(TAG, "showScannerDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .content("Silahkan pilih scanner yang ingin digunakan")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Bluetooth")
                .negativeText("QR")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), BluetoothActivity.class));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), QRActivity.class));
                    }
                })
                .canceledOnTouchOutside(true);

        scannerDialog = builder.build();
        scannerDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

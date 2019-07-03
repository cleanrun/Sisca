package com.siscaproject.sisca.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    private int sortNumber = 3;

    @BindView(R.id.cv_scanner) CardView cvScanner;
    @BindView(R.id.cv_search) CardView cvSearch;
    @BindView(R.id.btn_filter) CardView btnFilter;
    @BindView(R.id.rv_mutasi_terakhir) RecyclerView recyclerView;
    @BindView(R.id.pv_mutasi) ProgressView progressView;
    @BindView(R.id.ll_empty_mutasi)
    LinearLayout llEmpty;

    private UserService userService;

    private MaterialDialog scannerDialog;

    private AssetAdapter.cardClickListener clickListener = new AssetAdapter.cardClickListener() {
        @Override
        public void onCardClick(final int id) {
            Intent intent = new Intent(getActivity(), DetailMutationActivity.class);
            intent.putExtra("ID_ASSET_EXTRA", id);
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
                //showToast("Filter");
                filterData();
                break;
            default:
                break;
        }
    }

    private void filterData(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        CardView cvTerbaru = dialog.findViewById(R.id.cv_terbaru);
        CardView cvTerlama = dialog.findViewById(R.id.cv_terlama);
        CardView cvAtoZ = dialog.findViewById(R.id.cv_az);
        CardView cvZtoA = dialog.findViewById(R.id.cv_za);
        final ImageView ivTerbaru = (ImageView) dialog.findViewById(R.id.iv_terbaru);
        final ImageView ivTerlama = (ImageView) dialog.findViewById(R.id.iv_terlama);
        final ImageView ivAtoZ = (ImageView) dialog.findViewById(R.id.iv_az);
        final ImageView ivZtoA = (ImageView) dialog.findViewById(R.id.iv_za);

        ivTerbaru.setVisibility(View.INVISIBLE);
        ivTerlama.setVisibility(View.INVISIBLE);
        ivAtoZ.setVisibility(View.INVISIBLE);
        ivZtoA.setVisibility(View.INVISIBLE);

        switch (sortNumber){
            case 1 :
                ivTerbaru.setVisibility(View.VISIBLE);
                break;
            case 2 :
                ivTerlama.setVisibility(View.VISIBLE);
                break;
            case 3 :
                ivAtoZ.setVisibility(View.VISIBLE);
                break;
            case 4 :
                ivZtoA.setVisibility(View.VISIBLE);
                break;
        }

        cvTerbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                    @Override
                    public int compare(AssetMutasi locationAPI1, AssetMutasi locationAPI2) {
                        return locationAPI2.getUpdated_at().compareTo(locationAPI1.getUpdated_at());
                    }
                });

                sortNumber = 1;
                ivTerbaru.setVisibility(View.VISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        cvTerlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                    @Override
                    public int compare(AssetMutasi locationAPI1, AssetMutasi locationAPI2) {
                        return locationAPI1.getUpdated_at().compareTo(locationAPI2.getUpdated_at());
                    }
                });

                sortNumber = 2;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.VISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        cvAtoZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                    @Override
                    public int compare(AssetMutasi locationAPI1, AssetMutasi locationAPI2) {
                        return locationAPI1.getName().compareTo(locationAPI2.getName());
                    }
                });

                sortNumber = 3;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.VISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        cvZtoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                    @Override
                    public int compare(AssetMutasi locationAPI1, AssetMutasi locationAPI2) {
                        return locationAPI2.getName().compareTo(locationAPI1.getName());
                    }
                });

                sortNumber = 4;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.VISIBLE);
                showData();

                dialog.dismiss();
            }
        });
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

                    Collections.sort(listAsset, new Comparator<AssetMutasi>() {
                        @Override
                        public int compare(AssetMutasi locationAPI1, AssetMutasi locationAPI2) {
                            return locationAPI1.getName().compareTo(locationAPI2.getName());
                        }
                    });

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

        if (listAsset.isEmpty())
            llEmpty.setVisibility(View.VISIBLE);
        else
            llEmpty.setVisibility(View.INVISIBLE);

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

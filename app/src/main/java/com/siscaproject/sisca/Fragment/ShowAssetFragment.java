package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowAssetFragment extends DialogFragment {
    private static final String TAG = "ShowAssetFragment";

    private Asset asset;

    @BindView(R.id.btn_close) ImageButton btnClose;

    @BindView(R.id.tv_id) TextView tvId;

    @BindView(R.id.et_name) AppCompatEditText etName;
    @BindView(R.id.et_id) AppCompatEditText etId;
    @BindView(R.id.et_serial) AppCompatEditText etSerial;
    @BindView(R.id.et_purchase_date) AppCompatEditText etPurchaseDate;
    @BindView(R.id.et_order_number) AppCompatEditText etOrderNumber;
    @BindView(R.id.et_purchase_cost) AppCompatEditText etPurchaseCost;
    @BindView(R.id.et_warranty_months) AppCompatEditText etWarrantyMonths;

    public ShowAssetFragment() {
        // Required empty public constructor
    }

    public void setAsset(Asset asset){
        this.asset = asset;
    }

    public static ShowAssetFragment newInstance() {
        ShowAssetFragment fragment = new ShowAssetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_asset, container, false);
        ButterKnife.bind(this, view);

        tvId.setText(String.valueOf(asset.getId()));

        etName.setText(asset.getName());
        etId.setText(asset.getAsset_id());
        etSerial.setText(asset.getSerial());
        etPurchaseDate.setText(asset.getPurchase_date());
        etOrderNumber.setText(asset.getOrder_number());
        etPurchaseCost.setText(asset.getPurchase_cost());
        etWarrantyMonths.setText(asset.getWarranty_months());

        etName.setKeyListener(null);
        etId.setKeyListener(null);
        etSerial.setKeyListener(null);
        etPurchaseDate.setKeyListener(null);
        etOrderNumber.setKeyListener(null);
        etPurchaseCost.setKeyListener(null);
        etWarrantyMonths.setKeyListener(null);

        return view;
    }

    @OnClick(R.id.btn_close)
    public void onClick(View view){
        dismiss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

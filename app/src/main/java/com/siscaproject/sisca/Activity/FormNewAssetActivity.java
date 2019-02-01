package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormNewAssetActivity extends AppCompatActivity {
    private static final String TAG = "FormNewAssetActivity";

    @BindView(R.id.et_asset_name) EditText et_asset_name;
    @BindView(R.id.et_asset_tag) EditText et_asset_tag;
    @BindView(R.id.et_serial) EditText et_serial;
    @BindView(R.id.et_purchase_date) EditText et_purchase_date;
    @BindView(R.id.et_supplier) EditText et_supplier;
    @BindView(R.id.et_order_number) EditText et_order_number;
    @BindView(R.id.et_purchase_cost) EditText et_purchase_cost;
    @BindView(R.id.et_warranty_months) EditText et_warranty_months;
    @BindView(R.id.et_notes) EditText et_notes;

    @BindView(R.id.spn_company) Spinner spn_company;
    @BindView(R.id.spn_model) Spinner spn_model;
    @BindView(R.id.spn_status_label) Spinner spn_status_label;
    @BindView(R.id.spn_location) Spinner spn_location;

    @BindView(R.id.btn_upload) Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_asset);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        setAssetTag(extras);
    }

    private void setAssetTag(Bundle extras){
        if(extras != null){
            try{
                String assetTag = extras.getString("asset_tag", "null");
                et_asset_tag.setText(assetTag);
                et_asset_tag.setEnabled(false);
            }catch(Exception e){
                Log.e(TAG, "setAssetTag: " + e.getMessage());
            }
        }
        else{
            Log.i(TAG, "setAssetTag: extras is null");
        }
    }

}

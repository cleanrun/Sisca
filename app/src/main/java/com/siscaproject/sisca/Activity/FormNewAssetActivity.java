package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormNewAssetActivity extends AppCompatActivity {
    private static final String TAG = "FormNewAssetActivity";

    private UserService userService;

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

        userService = APIProperties.getUserService();

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

    private void createAsset(){
        String name = et_asset_name.getText().toString().trim();
        String assetTag = et_asset_tag.getText().toString().trim();
        String serial = et_serial.getText().toString().trim();
        String purchaseDate = et_purchase_date.getText().toString().trim();
        String supplier = et_supplier.getText().toString().trim();
        String orderNumber = et_order_number.getText().toString().trim();
        String purchaseCost = et_purchase_cost.getText().toString().trim();
        String warrantyMonths = et_warranty_months.getText().toString().trim();
        String notes = et_warranty_months.getText().toString().trim();

        String company = spn_company.getSelectedItem().toString();
        String model = spn_model.getSelectedItem().toString();
        String statusLabel = spn_status_label.getSelectedItem().toString();
        String location = spn_location.getSelectedItem().toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("asset_tag", assetTag);
        params.put("company_id", company);
        params.put("model_id", model);
        params.put("label_id", statusLabel);
        params.put("serial", serial);
        params.put("purchase_date", purchaseDate);
        params.put("supplier_id", supplier);
        params.put("order_number", orderNumber);
        params.put("purchase_cost", purchaseCost);
        params.put("warranty_months", warrantyMonths);
        params.put("notes", notes);
        params.put("location_id", location);
        params.put("image", "image.png");
        params.put("assigned_to", "admin");
    }

    // On progress
    private void addAsset(HashMap<String, String> params){
        String auth = Prefs.getString("token_type", "null")
                + " " + Prefs.getString("access_token", "null");
        String accept = "application/json";

        Call<Asset> call = userService.storeFixed(auth, accept, params);
        call.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                Log.i(TAG, "onResponse: " + response.toString());

            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

}

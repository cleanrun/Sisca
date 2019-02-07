package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.Company;
import com.siscaproject.sisca.Model.Label;
import com.siscaproject.sisca.Model.Location;
import com.siscaproject.sisca.Model.Model;
import com.siscaproject.sisca.Model.ResponseCompany;
import com.siscaproject.sisca.Model.ResponseLabel;
import com.siscaproject.sisca.Model.ResponseLocation;
import com.siscaproject.sisca.Model.ResponseModel;
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
    @BindView(R.id.btn_save) Button btn_save;

    private MaterialDialog loadingDialog;

    private ArrayList<Company> listCompany;
    private ArrayList<Model> listModel;
    private ArrayList<Label> listLabel;
    private ArrayList<Location> listLocation;

    private String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    private String accept = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_asset);

        setTitle("");

        ButterKnife.bind(this);

        userService = APIProperties.getUserService();

        Bundle extras = getIntent().getExtras();
        setAssetTag(extras);

        getCompany();
        getModel();
        getLabel();
        getLocation();
    }

    @OnClick(R.id.btn_save)
    public void onClick(View view){
        Log.i(TAG, "onClick: called");
        
        int id = view.getId();
        if(id == R.id.btn_save){
            createAsset();
        }
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

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(FormNewAssetActivity.this)
                .content("Sending data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loadingDialog = builder.build();
        loadingDialog.show();
    }

    private void dismissDialog(){
        loadingDialog.dismiss();
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

        Company companyObject = (Company) spn_company.getSelectedItem();
        Model modelObject = (Model) spn_model.getSelectedItem();
        Label labelObject = (Label) spn_status_label.getSelectedItem();
        Location locationObject = (Location) spn_location.getSelectedItem();

        String company = companyObject.getId();
        String model = modelObject.getId();
        String statusLabel = labelObject.getId();
        String location = locationObject.getId();

        Asset asset = new Asset();
        asset.setName(name);
        asset.setAsset_id(assetTag);
        asset.setSerial(serial);
        asset.setPurchase_date(purchaseDate);
        asset.setSupplier_id(supplier);
        asset.setOrder_number(orderNumber);
        asset.setPurchase_cost(purchaseCost);
        asset.setWarranty_months(warrantyMonths);
        asset.setNotes(notes);
        asset.setCompany_id(company);
        asset.setModel_id(model);
        asset.setLabel_id(statusLabel);
        asset.setLocation_id(location);

        addAsset(asset);
    }

    // TODO : On progress, success message still false. Check frequently for updates
    private void addAsset(Asset asset){
        showDialog();
        Call<Asset> call = userService.storeFixed(auth, accept, asset);
        call.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                dismissDialog();
                if(response.isSuccessful()){
                    Log.i(TAG, "addAsset onResponse: successful" + response.body());
                    Toast.makeText(FormNewAssetActivity.this, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Log.e(TAG, "addAsset onResponse: not successful " + response.errorBody().string());
                        Toast.makeText(FormNewAssetActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: exception");
                    }
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                dismissDialog();
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    private void getCompany(){
        Call<ResponseCompany> call = userService.indexCompany(auth, accept);
        call.enqueue(new Callback<ResponseCompany>() {
            @Override
            public void onResponse(Call<ResponseCompany> call, Response<ResponseCompany> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse company: company total " + response.body().getTotal());
                    listCompany = response.body().getRows();
                    //Log.i(TAG, listCompany.get(1).getName());

                    ArrayAdapter<Company> arrayAdapter = new ArrayAdapter<Company>(FormNewAssetActivity.this,
                            android.R.layout.simple_spinner_item, listCompany);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_company.setAdapter(arrayAdapter);
                }
                else{
                    Log.e(TAG, "onResponse company: else");

                }
            }

            @Override
            public void onFailure(Call<ResponseCompany> call, Throwable t) {
                Log.e(TAG, "onFailure company: " + t.getMessage() );
            }
        });
    }

    private void getModel(){
        Call<ResponseModel> call = userService.indexModel(auth, accept);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse model: model total " + response.body().getTotal());
                    listModel = response.body().getRows();
                    //Log.i(TAG, listModel.get(1).getName());

                    ArrayAdapter<Model> arrayAdapter = new ArrayAdapter<Model>(FormNewAssetActivity.this,
                            android.R.layout.simple_spinner_item, listModel);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_model.setAdapter(arrayAdapter);
                }
                else{
                    Log.e(TAG, "onResponse model: else");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure model: " + t.getMessage() );
            }
        });
    }

    private void getLabel(){
        Call<ResponseLabel> call = userService.indexLabel(auth, accept);
        call.enqueue(new Callback<ResponseLabel>() {
            @Override
            public void onResponse(Call<ResponseLabel> call, Response<ResponseLabel> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse label: label total " + response.body().getTotal());
                    listLabel = response.body().getRows();

                    ArrayAdapter<Label> arrayAdapter = new ArrayAdapter<Label>(FormNewAssetActivity.this,
                            android.R.layout.simple_spinner_item, listLabel);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_status_label.setAdapter(arrayAdapter);
                }
                else{
                    Log.e(TAG, "onResponse label: else");
                }
            }

            @Override
            public void onFailure(Call<ResponseLabel> call, Throwable t) {
                Log.e(TAG, "onFailure label: " + t.getMessage() );
            }
        });
    }

    private void getLocation(){
        Call<ResponseLocation> call = userService.indexLocation(auth, accept);
        call.enqueue(new Callback<ResponseLocation>() {
            @Override
            public void onResponse(Call<ResponseLocation> call, Response<ResponseLocation> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse location: location total " + response.body().getTotal());
                    listLocation = response.body().getRows();

                    ArrayAdapter<Location> arrayAdapter = new ArrayAdapter<Location>(FormNewAssetActivity.this,
                            android.R.layout.simple_spinner_item, listLocation);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_location.setAdapter(arrayAdapter);
                }
                else{
                    Log.e(TAG, "onResponse location: else");
                }
            }

            @Override
            public void onFailure(Call<ResponseLocation> call, Throwable t) {
                Log.e(TAG, "onFailure location: " + t.getMessage() );
            }
        });
    }
}

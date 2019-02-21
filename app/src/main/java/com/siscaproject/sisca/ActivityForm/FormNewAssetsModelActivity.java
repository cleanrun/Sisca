package com.siscaproject.sisca.ActivityForm;

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
import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.Model.Depreciation;
import com.siscaproject.sisca.Model.Manufacturer;
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

public class FormNewAssetsModelActivity extends AppCompatActivity {
    private static final String TAG = "FormNewAssetModel";

    private UserService userService;

    private MaterialDialog loadingDialog;

    @BindView(R.id.et_model_name) EditText etModelName;
    @BindView(R.id.et_model_number) EditText etModelNumber;
    @BindView(R.id.spn_manufacturer) Spinner spnManufacturer;
    @BindView(R.id.spn_category) Spinner spnCategory;
    @BindView(R.id.spn_depreciation) Spinner spnDepreciation;
    @BindView(R.id.btn_upload) Button btnUpload;
    @BindView(R.id.btn_save) Button btnSave;

    private ArrayList<Manufacturer> listManufacturer;
    private ArrayList<Category> listCategory;
    private ArrayList<Depreciation> listDepreciation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_assets_model);

        ButterKnife.bind(this);

        userService = APIProperties.getUserService();

        getManufacturer();
        getCategory();
        getDepreciation();
    }

    @OnClick({R.id.btn_save, R.id.btn_upload})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_upload:
                Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                //Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                createModel();
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(FormNewAssetsModelActivity.this)
                .content("Inputting data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loadingDialog = builder.build();
        loadingDialog.show();
    }

    private void createModel(){
        String modelName = etModelName.getText().toString().trim();
        String modelNumber = etModelNumber.getText().toString().trim();

        Category category = (Category) spnCategory.getSelectedItem();
        Manufacturer manufacturer = (Manufacturer) spnManufacturer.getSelectedItem();
        Depreciation depreciation = (Depreciation) spnDepreciation.getSelectedItem();

        Model model = new Model();
        model.setName(modelName);
        model.setModel_number(modelNumber);
        model.setCategory_id(category.getId());
        model.setManufacturer_id(manufacturer.getId());
        model.setDepreciation_id(depreciation.getId());

        addModel(model);
    }

    private void addModel(Model model){
        showDialog();
        Call<Model> call = userService.storeModel(Header.auth, Header.accept, model);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());
                    successDialog();
                }
                else{
                    try{
                        errorDialog();
                        Log.e(TAG, "onResponse: add model is not successfull " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: addModel " + t.getMessage() );
                errorDialog();
            }
        });
    }

    private void getManufacturer(){
        Call<ResponseIndex<Manufacturer>> call = userService.indexManufacturer(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Manufacturer>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Manufacturer>> call, Response<ResponseIndex<Manufacturer>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: manufacturer total " + response.body().getTotal());
                    listManufacturer = response.body().getRows();

                    ArrayAdapter<Manufacturer> arrayAdapter =
                            new ArrayAdapter<>(FormNewAssetsModelActivity.this,
                            android.R.layout.simple_spinner_item, listManufacturer);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnManufacturer.setAdapter(arrayAdapter);
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: manufacturer is not successful " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: manufacturer catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Manufacturer>> call, Throwable t) {
                Log.e(TAG, "onFailure: manufacturer " + t.getMessage());
            }
        });
    }

    private void getCategory(){
        Call<ResponseIndex<Category>> call = userService.indexCategory(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Category>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Category>> call, Response<ResponseIndex<Category>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: category total " + response.body().getTotal());
                    listCategory = response.body().getRows();

                    ArrayAdapter<Category> arrayAdapter =
                            new ArrayAdapter<>(FormNewAssetsModelActivity.this,
                                    android.R.layout.simple_spinner_item, listCategory);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCategory.setAdapter(arrayAdapter);
                }else{
                    try{
                        Log.e(TAG, "onResponse: category is not successful " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: category catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Category>> call, Throwable t) {
                Log.e(TAG, "onFailure: category " + t.getMessage());
            }
        });
    }

    private void getDepreciation(){
        Call<ResponseIndex<Depreciation>> call = userService.indexDepreciation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Depreciation>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Depreciation>> call, Response<ResponseIndex<Depreciation>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: depreciation total " + response.body().getTotal());
                    listDepreciation = response.body().getRows();

                    ArrayAdapter<Depreciation> arrayAdapter =
                            new ArrayAdapter<>(FormNewAssetsModelActivity.this,
                                    android.R.layout.simple_spinner_item, listDepreciation);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnDepreciation.setAdapter(arrayAdapter);
                }else{
                    try{
                        Log.e(TAG, "onResponse: depreciation is not successful " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: depreciation catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<Depreciation>> call, Throwable t) {
                Log.e(TAG, "onFailure: depreciation " + t.getMessage());
            }
        });
    }

    private void errorDialog(){
        Toast.makeText(FormNewAssetsModelActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
    }

    private void successDialog(){
        Toast.makeText(FormNewAssetsModelActivity.this, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
    }
}

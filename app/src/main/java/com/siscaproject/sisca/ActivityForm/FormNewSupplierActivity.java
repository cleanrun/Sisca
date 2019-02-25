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
import com.siscaproject.sisca.Model.Supplier;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormNewSupplierActivity extends AppCompatActivity {
    private static final String TAG = "FormNewSupplier";

    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_city) EditText etCity;
    @BindView(R.id.et_address) EditText etAddress;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_zip) EditText etZip;
    @BindView(R.id.et_url) EditText etUrl;

    @BindView(R.id.spn_state) Spinner spnState;
    @BindView(R.id.spn_country) Spinner spnCountry;

    @BindView(R.id.btn_upload) Button btnUpload;
    @BindView(R.id.btn_save) Button btnSave;

    private UserService userService;

    private MaterialDialog loadingDialog;

    private static final String[] listState = {"State 1", "State 2", "State 3", "State 4"};
    private static final String[] listCountry = {"Country 1", "Country 2", "Country 3", "Country 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_supplier);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        initSpinner();
    }

    private void initSpinner(){
        ArrayAdapter<String> arrayAdapterState = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listState);
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnState.setAdapter(arrayAdapterState);

        ArrayAdapter<String> arrayAdapterCountry = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listCountry);
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(arrayAdapterState);
    }

    @OnClick({R.id.btn_save, R.id.btn_upload})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_upload:
                Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                createSupplier();
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Inputting data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loadingDialog = builder.build();
        loadingDialog.show();
    }

    private void createSupplier(){
        String name = etName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String url = etUrl.getText().toString().trim();

        String state = spnState.getSelectedItem().toString();
        String country = spnCountry.getSelectedItem().toString();

        Supplier supplier = new Supplier(name, address, city, state, country, phone, email, zip, url);
        addSupplier(supplier);
    }

    private void addSupplier(Supplier supplier){
        showDialog();

        Call<Supplier> call = userService.storeSupplier(Header.auth, Header.accept, supplier);
        call.enqueue(new Callback<Supplier>() {
            @Override
            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());
                    successDialog();
                }
                else{
                    try{
                        errorDialog();
                        Log.e(TAG, "onResponse: add supplier is not successfull " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: addModel " + t.getMessage() );
                errorDialog();
            }
        });
    }

    private void errorDialog(){
        Toast.makeText(this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
    }

    private void successDialog(){
        Toast.makeText(this, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
    }

}
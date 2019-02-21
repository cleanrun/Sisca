package com.siscaproject.sisca.ActivityForm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.Model.Manufacturer;
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

public class FormNewManufacturerActivity extends AppCompatActivity {
    private static final String TAG = "FormNewManufacturer";

    private UserService userService;

    private MaterialDialog loadingDialog;

    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_url) EditText etUrl;
    @BindView(R.id.et_support_url) EditText etSupportUrl;
    @BindView(R.id.et_support_phone) EditText etSupportPhone;
    @BindView(R.id.et_support_email) EditText etSupportEmail;

    @BindView(R.id.btn_upload) Button btnUpload;
    @BindView(R.id.btn_save) Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_manufacturer);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_upload, R.id.btn_save})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_upload:
                Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                //Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                createManufacturer();
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

    private void createManufacturer(){
        String name = etName.getText().toString();
        String url = etUrl.getText().toString();
        String supportUrl = etSupportUrl.getText().toString();
        String supportPhone = etSupportPhone.getText().toString();
        String supportEmail = etSupportEmail.getText().toString();

        Manufacturer manufacturer = new Manufacturer(name, url, supportUrl, supportPhone, supportEmail);
        addManufacturer(manufacturer);
    }

    private void addManufacturer(Manufacturer manufacturer){
        showDialog();

        Call<Manufacturer> call = userService.storeManufacturer(Header.auth, Header.accept, manufacturer);
        call.enqueue(new Callback<Manufacturer>() {
            @Override
            public void onResponse(Call<Manufacturer> call, Response<Manufacturer> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());
                    successDialog();
                }
                else{
                    try{
                        errorDialog();
                        Log.e(TAG, "onResponse: add manufacturer is not successful " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Manufacturer> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage() );
                errorDialog();
            }
        });
    }

    private void errorDialog(){
        Toast.makeText(FormNewManufacturerActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
    }

    private void successDialog(){
        Toast.makeText(FormNewManufacturerActivity.this, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
    }
}

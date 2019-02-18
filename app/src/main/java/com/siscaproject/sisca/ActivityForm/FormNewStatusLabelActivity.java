package com.siscaproject.sisca.ActivityForm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.Model.Label;
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

public class FormNewStatusLabelActivity extends AppCompatActivity {
    private static final String TAG = "FormNewStatusLabel";

    private UserService userService;

    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_type) EditText etType;
    @BindView(R.id.et_notes) EditText etNotes;

    private MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_status_label);

        ButterKnife.bind(this);

        userService = APIProperties.getUserService();
    }

    @OnClick(R.id.btn_save)
    public void onClick(View view){
        if(view.getId() == R.id.btn_save){
            //Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
            createLabel();
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

    private void createLabel(){
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        Label label = new Label(name, type, notes);
        addLabel(label);
    }

    private void addLabel(Label label){
        showDialog();

        Call<Label> call = userService.storeLabel(Header.auth, Header.accept, label);
        call.enqueue(new Callback<Label>() {
            @Override
            public void onResponse(Call<Label> call, Response<Label> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());
                    successToast();
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: not successful " + response.errorBody().string() );
                        errorToast();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Label> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast(){
        Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void successToast() {
        Toast.makeText(this, "Data successfully updated!", Toast.LENGTH_SHORT).show();
    }
}

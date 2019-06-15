package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.siscaproject.sisca.Model.LoginAuth;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    private static UserService userService;

    @BindView(android.R.id.content) View parent_view;

    @BindView(R.id.textinput_username) TextInputEditText username;
    @BindView(R.id.textinput_address) TextInputEditText address;
    @BindView(R.id.textinput_password) ShowHidePasswordEditText password;

    @BindView(R.id.textview_forgot_password) TextView forgotPassword;

    @BindView(R.id.btn_login) Button btnLogin;

    @BindView(R.id.pb_login) ProgressBar progressBar;

    private MaterialDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        userService = APIProperties.getUserService();

        // TODO: set address function has yet to be created

        new Prefs.Builder()
                .setContext(this)
                .setMode(android.content.ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @OnClick ({R.id.btn_login, R.id.textview_forgot_password})
    public void onClickBind(View view){
        int id = view.getId();

        switch(id){
            case R.id.btn_login:
                logIn();
                //startActivity(new Intent(LoginActivity.this, HomeNavigationActivity.class));
                break;
            case R.id.textview_forgot_password:
                Snackbar.make(parent_view, "Forgot Password", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                Snackbar.make(parent_view, "???", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(LoginActivity.this)
                .content("Logging in..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loginDialog = builder.build();
        loginDialog.show();
    }

    private void logIn(){
        if(TextUtils.isEmpty(username.getText().toString().trim())){
            username.setError("Username can not be empty");
            username.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(password.getText().toString().trim())){
            password.setError("Password can not be empty");
            password.requestFocus();
            return;
        }
        else{
            getLogin(username.getText().toString(), password.getText().toString() );
        }
    }

    private void getLogin(String email, String password) {
        showDialog();
        Call<LoginAuth> call = userService.getLogin(email, password);
        call.enqueue(new Callback<LoginAuth>() {
            @Override
            public void onResponse(Call<LoginAuth> call, Response<LoginAuth> response) {
                try {
                    if (response.body().getToken_type().equals("Bearer")) {
                        Log.i(TAG, "getLogin, Authorized access");
                        Log.i(TAG, "getLogin, access_token: " + response.body().getAccess_token().toString());
                        Log.i(TAG, "getLogin, expires_at: " + response.body().getExpires_at().toString());

                        Prefs.putInt("id", response.body().getUser().getId());
                        Prefs.putString("name", response.body().getUser().getName().toString());
                        Prefs.putString("email", response.body().getUser().getEmail().toString());
                        Prefs.putString("access_token", response.body().getAccess_token().toString());
                        Prefs.putString("token_type", response.body().getToken_type().toString());
                        Prefs.putString("expires_at", response.body().getExpires_at().toString());

                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Log.e(TAG, "getLogin, Unauthorized access" + response.body().getToken_type().toString());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getLogin exception " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
                dismissDialog();
            }

            @Override
            public void onFailure(Call<LoginAuth> call, Throwable t) {
                Log.e(TAG, "getLogin, onFailure : " + t.getMessage());
                dismissDialog();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Unable to Log In", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void dismissDialog(){
        loginDialog.dismiss();
    }

}

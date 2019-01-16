package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.ViewAnimation;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "HomeActivity";

    @BindView(R.id.lyt_parent_home) NestedScrollView parentLayout;
    @BindView(R.id.btn_toggle_features) ImageButton btnToggleFeatures;
    @BindView(R.id.btn_toggle_profile) ImageButton btnToggleProfile;
    @BindView(R.id.btn_toggle_settings) ImageButton btnToggleSettings;
    @BindView(R.id.btn_back) AppCompatImageButton btnBack;
    @BindView(R.id.lyt_expand_features) View lytExpandFeatures;
    @BindView(R.id.lyt_expand_profile) View lytExpandProfile;
    @BindView(R.id.lyt_expand_settings) View lytExpandSettings;

    @BindView(R.id.lyt_bluetooth_feature) LinearLayout lytBluetoothFeature;
    @BindView(R.id.lyt_qr_feature) LinearLayout lytQrFeature;
    @BindView(R.id.lyt_input_feature) LinearLayout lytInputFeature;
    @BindView(R.id.lyt_extra_feature) LinearLayout lytExtraFeature;

    @BindView(R.id.tv_todays_date) TextView todaysDate;

    @BindView(R.id.toolbar_home) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle("");

        btnToggleFeatures.setOnClickListener(this);
        btnToggleProfile.setOnClickListener(this);
        btnToggleSettings.setOnClickListener(this);

        continuousDate();
    }

    private void continuousDate(){
        Thread t = new Thread(){
            @Override
            public void run() {
                try{
                    while(!isInterrupted()){
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a");
                                String dateString = sdf.format(date);
                                todaysDate.setText(dateString);
                            }
                        });
                    }
                }catch(Exception e){
                    Log.e(TAG, "continuousDate: " + e.getMessage());
                }
            }
        };
        t.start();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_toggle_features:
                toggleSection(view, lytExpandFeatures);
                break;
            case R.id.btn_toggle_profile:
                toggleSection(view, lytExpandProfile);
                break;
            case R.id.btn_toggle_settings:
                toggleSection(view, lytExpandSettings);
                break;
            default:
                break;

        }
    }

    @OnClick({R.id.lyt_bluetooth_feature, R.id.lyt_qr_feature, R.id.lyt_input_feature, R.id.lyt_extra_feature, R.id.btn_back})
    public void onClickFeatures(View view){
        switch(view.getId()){
            case R.id.lyt_bluetooth_feature:
                try{
                    Intent btIntent = new Intent(HomeActivity.this, BluetoothActivity.class);
                    startActivity(btIntent);
                }catch (Exception e){
                    Toast.makeText(HomeActivity.this, "Unexpected mistake", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onCLickFeatures: " + e.getMessage());
                }
                break;
            case R.id.lyt_qr_feature:
                try{
                    Log.d(TAG, "onCLickFeatures: QR Reader clicked");
                    Intent qrIntent = new Intent(HomeActivity.this, QRActivity.class);
                    startActivity(qrIntent);
                }catch (Exception e){
                    Toast.makeText(HomeActivity.this, "Unexpected mistake", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onCLickFeatures: " + e.getMessage());
                }
                break;
            case R.id.lyt_input_feature:
                Log.d(TAG, "onCLickFeatures: Manual Input clicked");
                Toast.makeText(HomeActivity.this, "Manual Input", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lyt_extra_feature:
                Log.d(TAG, "onCLickFeatures: Extra Reader clicked");
                Intent testIntent = new Intent(HomeActivity.this, ExtraActivity.class);
                startActivity(testIntent);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public void toggleSection(View button, final View layout){
        boolean show = toogleArrow(button);
        if(show){
            ViewAnimation.expand(layout, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    parentLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            parentLayout.scrollTo(500, layout.getBottom());
                        }
                    });
                }
            });
        }else{
            ViewAnimation.collapse(layout);
        }
    }

    public boolean toogleArrow(View view){
        if(view.getRotation() == 0){
            view.animate().setDuration(200).rotation(180);
            return true;
        }else{
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

}

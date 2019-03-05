package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.siscaproject.sisca.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2500);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException e){
                    //e.printStackTrace();
                    System.exit(1);
                }
            }
        };
        splashThread.start();
    }
}

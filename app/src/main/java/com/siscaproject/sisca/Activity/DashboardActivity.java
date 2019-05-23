package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Fragment.HomeFragment;
import com.siscaproject.sisca.Fragment.MonitoringFragment;
import com.siscaproject.sisca.Fragment.MutasiFragment;
import com.siscaproject.sisca.Fragment.ProfileFragment;
import com.siscaproject.sisca.R;

public class DashboardActivity extends AppCompatActivity implements MonitoringFragment.OnFragmentInteractionListener,
        MutasiFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initNavigation();
        checkLoginStatus();

        if(savedInstanceState == null){
            navigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void initNavigation(){
        navigationView = findViewById(R.id.btm_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = HomeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Dashboard");
                        break;
                    case R.id.nav_monitoring:
                        selectedFragment = MonitoringFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Monitoring");
                        break;
                    case R.id.nav_pindah_aset:
                        selectedFragment = MutasiFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Pindah Aset");
                        break;
                    case R.id.nav_profil:
                        selectedFragment = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Profil");
                        break;
                }

                return true;
            }
        });
    }

    private void checkLoginStatus(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(android.content.ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        String stat = Prefs.getString("access_token", "null");

        if (stat.equals("null")) {
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

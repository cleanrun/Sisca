package com.siscaproject.sisca.Activity;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.siscaproject.sisca.Fragment.AssetsFragment;
import com.siscaproject.sisca.Fragment.HomeFragment;
import com.siscaproject.sisca.Fragment.ProfileFragment;
import com.siscaproject.sisca.Fragment.ReportFragment;
import com.siscaproject.sisca.R;

public class HomeNavigationActivity extends AppCompatActivity
    implements AssetsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
                ProfileFragment.OnFragmentInteractionListener, ReportFragment.OnFragmentInteractionListener{

    private static final String TAG = "HomeNavigationActivity";

    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        initComponents();

        if(savedInstanceState == null){
            navigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    public void initComponents(){

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
                        break;
                    case R.id.nav_assets:
                        selectedFragment = AssetsFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        break;
                    case R.id.nav_report:
                        selectedFragment = ReportFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        break;
                    case R.id.nav_settings:
                        // Settings fragment has yet to be created
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Still null
    }
}

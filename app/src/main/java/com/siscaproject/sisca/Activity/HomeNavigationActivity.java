package com.siscaproject.sisca.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Fragment.AssetsFragment;
import com.siscaproject.sisca.Fragment.HomeFragment;
import com.siscaproject.sisca.Fragment.ProfileFragment;
import com.siscaproject.sisca.Fragment.ReportFragment;
import com.siscaproject.sisca.Fragment.SearchAssetFragment;
import com.siscaproject.sisca.Fragment.SettingsFragment;
import com.siscaproject.sisca.R;

public class HomeNavigationActivity extends AppCompatActivity
    implements AssetsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
                ProfileFragment.OnFragmentInteractionListener, ReportFragment.OnFragmentInteractionListener,
                SettingsFragment.OnFragmentInteractionListener, SearchAssetFragment.OnFragmentInteractionListener{

    private static final String TAG = "HomeNavigationActivity";

    private BottomNavigationView navigationView;
    private Toolbar toolbar;

    private MenuItem aboutMenu;
    private MenuItem logoutMenu;

    private MaterialDialog logoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbar.setLogo(R.drawable.sisca_logo_white);

        initComponents();
        checkLoginStatus();

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
                        setTitle("Dashboard");
                        break;
                    case R.id.nav_assets:
                        selectedFragment = AssetsFragment.newInstance();
                        //selectedFragment = SearchAssetFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Assets");
                        break;
                    case R.id.nav_report:
                        selectedFragment = ReportFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Report");
                        break;
                    case R.id.nav_profile:
                        selectedFragment = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Profile");
                        break;
                    case R.id.nav_settings:
                        selectedFragment = SettingsFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nsv_container_home, selectedFragment).commit();
                        setTitle("Settings");
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        aboutMenu = menu.findItem(R.id.menu_about);
        logoutMenu = menu.findItem(R.id.menu_logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menu_logout:
                showLogoutDialog();
                break;
            case R.id.menu_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        Log.i(TAG, "showLogoutDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Are you sure you want to Log Out?")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Prefs.clear();
                        finish();
                        moveTaskToBack(true);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // Do nothing
                    }
                })
                .canceledOnTouchOutside(true);

        logoutDialog = builder.build();
        logoutDialog.show();
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
        // Still null
    }
}

package com.siscaproject.sisca.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.BuildConfig;
import com.siscaproject.sisca.Fragment.ScanFragment;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.FamsModel;
import com.siscaproject.sisca.Utilities.ModelBase;
import com.siscaproject.sisca.Utilities.TSLBluetoothDeviceActivity;
import com.siscaproject.sisca.Utilities.TSLBluetoothDeviceApplication;
import com.siscaproject.sisca.Utilities.WeakHandler;
import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;
import com.uk.tsl.rfid.asciiprotocol.commands.FactoryDefaultsCommand;
import com.uk.tsl.rfid.asciiprotocol.responders.LoggerResponder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothActivity extends TSLBluetoothDeviceActivity{
    private static final String TAG = "BluetoothActivity";

    // private String stat;
    private BluetoothAdapter bluetoothAdapter;
    private FamsModel famsModel;
    private boolean isReaderConnected = false;
    private MaterialDialog dialog;
    String connectionMessage;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    private static final int REQUEST_ENABLE_BT = 3;

    // Debug control
    private static final boolean D = BuildConfig.DEBUG;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.viewpager_bluetooth) ViewPager mViewPager;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_bluetooth) TabLayout mTabLayout;
    @BindView(R.id.tv_reader_state) TextView tvReaderState;

    // Menu items
    private MenuItem reconnectMenuItem;
    private MenuItem connectMenuItem;
    private MenuItem disconnectMenuItem;
    private MenuItem resetMenuItem;

    public AsciiCommander getCommander(){
        return ((TSLBluetoothDeviceApplication) getApplication()).getCommander();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        setupViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);

        AsciiCommander commander = getCommander();

        // Add the LoggerResponder - this simply echoes all lines received from the reader to the log
        // and passes the line onto the next responder
        // This is added first so that no other responder can consume received lines before they are logged.
        commander.addResponder(new LoggerResponder());

        // Add a synchronous responder to handle synchronous commands
        commander.addSynchronousResponder();

        famsModel = new FamsModel();
        famsModel.setCommander(getCommander());
        famsModel.setHandler(mGenericModelHandler);
    }

    public void findDevice() {
        try {
            Log.d(TAG, "findDevice: called");
            selectDevice();
        }catch (Exception e) {
            Log.e(TAG, "findDevice: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Unable to find device", Toast.LENGTH_SHORT).show();
        }
    }


    public void doScan(){
        try {
            Log.d(TAG, "doScan: called");
            famsModel.scan();
        }catch (Exception e) {
            Log.e(TAG, "doScan: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Unable to scan", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetReader() {
        try {
            // Reset the reader
            Log.d(TAG, "resetReader: called");
            FactoryDefaultsCommand fdCommand = FactoryDefaultsCommand.synchronousCommand();
            getCommander().executeCommand(fdCommand);
            String msg = "Reset " + (fdCommand.isSuccessful() ? "succeeded" : "Failed");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "resetReader: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Unable to reset the reader", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScanFragment(), "REGISTER");
        //adapter.addFragment(new SearchFragment(), "SEARCH");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);

        reconnectMenuItem = menu.findItem(R.id.menu_item_reconnect_reader);
        connectMenuItem = menu.findItem(R.id.menu_item_secure_connect_reader);
        disconnectMenuItem = menu.findItem(R.id.menu_item_disconnect_reader);
        resetMenuItem = menu.findItem(R.id.menu_item_reset_reader);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.menu_item_reconnect_reader:
                Toast.makeText(this.getApplicationContext(), "Reconnecting..", Toast.LENGTH_LONG).show();
                reconnectDevice();
                return true;
            case R.id.menu_item_insecure_connect_reader:
                // Choose a device and connect to it

                selectDevice();
                return true;
            case R.id.menu_item_disconnect_reader:
                Toast.makeText(this.getApplicationContext(), "Disconnecting..", Toast.LENGTH_SHORT).show();
                disconnectDevice();
                return true;
            case R.id.menu_item_reset_reader:
                resetReader();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if(resultCode != Activity.RESULT_OK){
                    // User did not enable Bluetooth or an error occurred
                    bluetoothNotAvailableError("Bluetooth was not enabled\nApplication Quitting..");
                }
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceLisActivity returns with a device to connect
                if(resultCode == Activity.RESULT_OK){
                    connectToDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if(resultCode == Activity.RESULT_OK){
                    connectToDevice(data, false);
                }
                break;
        }
    }

    private void displayReaderState(){
        connectionMessage = "Reader ";
        switch(getCommander().getConnectionState()){
            case CONNECTED:
                isReaderConnected = true;
                connectionMessage += getCommander().getConnectedDeviceName();
                //getSupportActionBar().setTitle(connectionMessage);
                tvReaderState.setText(connectionMessage);
                break;
            case CONNECTING:
                connectionMessage += "Connecting..";
                //getSupportActionBar().setTitle(connectionMessage);
                tvReaderState.setText(connectionMessage);
                break;
            default:
                isReaderConnected= false;
                connectionMessage += "Disconnected";
                //getSupportActionBar().setTitle(connectionMessage);
                tvReaderState.setText(connectionMessage);
        }
    }

    private final WeakHandler<BluetoothActivity> mGenericModelHandler = new WeakHandler<BluetoothActivity>(this) {
        @Override
        public void handleMessage(Message msg, BluetoothActivity bluetoothActivity) {
            try{
                switch(msg.what){
                    case ModelBase.BUSY_STATE_CHANGED_NOTIFICATION:
                        break;
                    case ModelBase.MESSAGE_NOTIFICATION:
                        // Examine the message for prefix
                        String message = (String) msg.obj;

                        if(message.startsWith("ER:")){
                            Toast.makeText(BluetoothActivity.this, message.substring(3), Toast.LENGTH_SHORT).show();
                        }
                        else if(message.startsWith("BC:")){
                            int index = mViewPager.getCurrentItem();
                            SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
                            Fragment fragment = adapter.getFragment(index);

                            if(fragment instanceof ScanFragment){
                                ScanFragment r = (ScanFragment) fragment;
                                r.addData(message);
                            }

                        }
                        else if(message.startsWith("EPC")){
                            int index = mViewPager.getCurrentItem();
                            SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
                            Fragment fragment = adapter.getFragment(index);

                            if(fragment instanceof ScanFragment){
                                ScanFragment r = (ScanFragment) fragment;
                                r.addData(message);
                            }

                        }
                        break;
                    default:
                        break;

                }
            }catch(Exception e){
                Log.d(TAG, "mGenericModelHandler : " + e.getMessage());
                Toast.makeText(BluetoothActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private BroadcastReceiver mCommanderMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(D){
                Log.d(TAG, "mCommanderMessageReceiver : AsciiCommander state changed - isConnected: "
                        + getCommander().isConnected());
            }

            String connectionStateMessage = intent.getStringExtra(AsciiCommander.REASON_KEY);
            Toast.makeText(context, connectionStateMessage, Toast.LENGTH_SHORT).show();

            displayReaderState();
            if(getCommander().isConnected()){
                famsModel.resetDevice();
                famsModel.updateConfiguration();
            }
        }
    };

    public String getStatus(){
        return connectionMessage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        famsModel.setEnabled(true);

        // Register to receive notifications from the AsciiCommander
        LocalBroadcastManager.getInstance(BluetoothActivity.this).registerReceiver(mCommanderMessageReceiver,
                new IntentFilter(AsciiCommander.STATE_CHANGED_NOTIFICATION));

        displayReaderState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        famsModel.setEnabled(false);

        // Unregister to receive notifications from the AsciiCommander
        LocalBroadcastManager.getInstance(BluetoothActivity.this).unregisterReceiver(mCommanderMessageReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        famsModel.setEnabled(false);

        // Unregister to receive notifications from the AsciiCommander
        LocalBroadcastManager.getInstance(BluetoothActivity.this).unregisterReceiver(mCommanderMessageReceiver);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragmentList = new ArrayList<>();
        private ArrayList<String> fragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title ){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public Fragment getFragment(int id){
            return fragmentList.get(id);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
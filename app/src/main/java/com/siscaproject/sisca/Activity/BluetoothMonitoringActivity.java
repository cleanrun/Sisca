package com.siscaproject.sisca.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.BuildConfig;
import com.siscaproject.sisca.Fragment.BluetoothMonitoringFragment;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.FamsModel;
import com.siscaproject.sisca.Utilities.ModelBase;
import com.siscaproject.sisca.Utilities.TSLBluetoothDeviceActivity;
import com.siscaproject.sisca.Utilities.TSLBluetoothDeviceApplication;
import com.siscaproject.sisca.Utilities.WeakHandler;
import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;
import com.uk.tsl.rfid.asciiprotocol.commands.FactoryDefaultsCommand;
import com.uk.tsl.rfid.asciiprotocol.responders.LoggerResponder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothMonitoringActivity extends TSLBluetoothDeviceActivity {
    private static final String TAG = "BluetoothMonitoringAct";

    private BluetoothAdapter bluetoothAdapter;
    private FamsModel famsModel;
    private boolean isReaderConnected = false;
    private MaterialDialog dialog;
    String connectionMessage;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    private static final int REQUEST_ENABLE_BT = 3;

    private static final boolean D = BuildConfig.DEBUG;

    private BluetoothActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private ReportMonitoringActivity reportMonitoringActivity;

    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.cv_back) CardView cvBack;
    @BindView(R.id.cv_menu) CardView cvMenu;

    public AsciiCommander getCommander(){
        return ((TSLBluetoothDeviceApplication) getApplication()).getCommander();
    }

    private final WeakHandler<BluetoothMonitoringActivity> mGenericModelHandler = new WeakHandler<BluetoothMonitoringActivity>(this) {
        @Override
        public void handleMessage(Message msg, BluetoothMonitoringActivity bluetoothMonitoringActivity) {
            try{
                switch(msg.what){
                    case ModelBase.BUSY_STATE_CHANGED_NOTIFICATION:
                        break;
                    case ModelBase.MESSAGE_NOTIFICATION:
                        String message = (String) msg.obj;
                        if(message.startsWith("ER:")){
                            showToast(message.substring(3));
                        }
                        else if(message.startsWith("BC:")){
                            int index = viewPager.getCurrentItem();
                            BluetoothActivity.SectionsPagerAdapter adapter = (BluetoothActivity.SectionsPagerAdapter) viewPager.getAdapter();
                            Fragment fragment = adapter.getFragment(index);

                            if(fragment instanceof BluetoothMonitoringFragment){
                                BluetoothMonitoringFragment r = (BluetoothMonitoringFragment) fragment;
                                r.addData(message);
                            }
                        }
                        else if(message.startsWith("EPC")){
                            int index = viewPager.getCurrentItem();
                            BluetoothActivity.SectionsPagerAdapter adapter = (BluetoothActivity.SectionsPagerAdapter) viewPager.getAdapter();
                            Fragment fragment = adapter.getFragment(index);

                            if(fragment instanceof BluetoothMonitoringFragment){
                                BluetoothMonitoringFragment r = (BluetoothMonitoringFragment) fragment;
                                r.addData(message);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }catch(Exception e){
                Log.e(TAG, "handleMessage: " + e.getMessage() );
                showToast("Something wrong just happened :(");
            }
        }
    };

    private BroadcastReceiver messageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(D){
                Log.d(TAG, "messageReceiver : AsciiCommander state changed - isConnected: "
                        + getCommander().isConnected());
            }

            String connectionStateMessage = intent.getStringExtra(AsciiCommander.REASON_KEY);
            showToast(connectionStateMessage);

            displayReaderState();
            if(getCommander().isConnected()){
                famsModel.resetDevice();
                famsModel.updateConfiguration();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_monitoring);

        ButterKnife.bind(this);

        mSectionsPagerAdapter = new BluetoothActivity.SectionsPagerAdapter(getSupportFragmentManager());
        setupViewPager(viewPager);

        AsciiCommander commander = getCommander();
        commander.addResponder(new LoggerResponder());
        commander.addSynchronousResponder();

        famsModel = new FamsModel();
        famsModel.setCommander(getCommander());
        famsModel.setHandler(mGenericModelHandler);

    }

    public void setReportMonitoringActivity(ReportMonitoringActivity reportMonitoringActivity){
        this.reportMonitoringActivity = reportMonitoringActivity;
    }

    @OnClick(R.id.cv_menu)
    public void optionsMenu(){
        Log.d(TAG, "optionsMenu");
        PopupMenu pm = new PopupMenu(this, cvMenu);
        pm.getMenuInflater().inflate(R.menu.menu_bluetooth, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.menu_item_reconnect_reader:
                        showToast("Reconnecting..");
                        reconnectDevice();
                        return true;
                    case R.id.menu_item_insecure_connect_reader:
                        selectDevice();
                        return true;
                    case R.id.menu_item_disconnect_reader:
                        showToast("Disconnecting..");
                        disconnectDevice();
                        return true;
                    case R.id.menu_item_reset_reader:
                        resetReader();
                        return true;
                }
                return false;
            }
        });
        pm.show();
    }

    @OnClick(R.id.cv_back)
    public void back(){
        Log.d(TAG, "back");
        onBackPressed();
    }


    public void findDevice(){
        try{
            Log.d(TAG, "findDevice: called");
            selectDevice();
        }catch(Exception e){
            Log.e(TAG, "findDevice: " + e.getMessage() );
            showToast("Unable to find device");
        }
    }

    public void doScan(){
        try{
            Log.d(TAG, "doScan: called");
            famsModel.scan();
        }catch(Exception e){
            Log.e(TAG, "doScan: " + e.getMessage() );
            showToast("Unable to scan device");
        }
    }

    private void resetReader(){
        try {
            Log.d(TAG, "resetReader: called");
            FactoryDefaultsCommand fdCommand = FactoryDefaultsCommand.synchronousCommand();
            getCommander().executeCommand(fdCommand);
            String msg = "Reset " + (fdCommand.isSuccessful() ? "succeeded" : "failed");
            showToast(msg);
        }catch (Exception e){
            Log.e(TAG, "resetReader: " + e.getMessage() );
            showToast("Unable to reset the reader");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode != Activity.RESULT_OK){
                    bluetoothNotAvailableError("Bluetooth was not enabled\nReturning to last activity..");
                }
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                if(resultCode == Activity.RESULT_OK){
                    connectToDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if(resultCode == Activity.RESULT_OK){
                    connectToDevice(data, false);
                }
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager){
        BluetoothActivity.SectionsPagerAdapter adapter = new BluetoothActivity.SectionsPagerAdapter(getSupportFragmentManager());
        BluetoothMonitoringFragment fragment = new BluetoothMonitoringFragment();
        fragment.setReportMonitoringActivity(reportMonitoringActivity);
        adapter.addFragment(fragment, "MAIN");
        viewPager.setAdapter(adapter);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void displayReaderState(){
        connectionMessage = "Reader ";
        switch(getCommander().getConnectionState()){
            case CONNECTED:
                isReaderConnected = true;
                connectionMessage += getCommander().getConnectedDeviceName();
                //getSupportActionBar().setTitle(connectionMessage);
                //tvReaderState.setText(connectionMessage);
                break;
            case CONNECTING:
                connectionMessage += "Connecting..";
                //getSupportActionBar().setTitle(connectionMessage);
                //tvReaderState.setText(connectionMessage);
                break;
            default:
                isReaderConnected= false;
                connectionMessage += "Disconnected";
                //getSupportActionBar().setTitle(connectionMessage);
                //tvReaderState.setText(connectionMessage);
        }
    }

    public String getStatus(){
        return connectionMessage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        famsModel.setEnabled(true);
        LocalBroadcastManager.getInstance(BluetoothMonitoringActivity.this).registerReceiver(messageReciever,
                new IntentFilter(AsciiCommander.STATE_CHANGED_NOTIFICATION));
        displayReaderState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        famsModel.setEnabled(false);
        LocalBroadcastManager.getInstance(BluetoothMonitoringActivity.this).unregisterReceiver(messageReciever);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(BluetoothMonitoringActivity.this).unregisterReceiver(messageReciever);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
            // do nothing
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
            return rootView;
        }
    }
}

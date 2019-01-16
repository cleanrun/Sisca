package com.siscaproject.sisca.Utilities;

import android.app.Application;

import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;


public class TSLBluetoothDeviceApplication extends Application {
    private static AsciiCommander commander = null;

    /// Returns the current AsciiCommander
    public AsciiCommander getCommander() {
        return commander;
    }

    /// Sets the current AsciiCommander
    public void setCommander(AsciiCommander _commander) {
        commander = _commander;
    }

}

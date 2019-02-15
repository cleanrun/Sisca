package com.siscaproject.sisca.Utilities;

import com.pixplicity.easyprefs.library.Prefs;

public class Header {
    public static final String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    public static final String accept = "application/json";

}

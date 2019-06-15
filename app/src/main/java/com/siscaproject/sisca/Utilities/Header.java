package com.siscaproject.sisca.Utilities;

import com.pixplicity.easyprefs.library.Prefs;

public class Header {
    public static final String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    public static final String accept = "application/json";

    public static final int id = Prefs.getInt("id", 0);
    public static final String name = Prefs.getString("name", "null");
    public static final String email = Prefs.getString("email", "null");

}

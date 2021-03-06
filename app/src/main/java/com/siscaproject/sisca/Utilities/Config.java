package com.siscaproject.sisca.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Config {
    public static final String API_URL = "http://dev.sisca.id/api/v1/";

    //public final static String CLIENT_SECRET = "krrUUmbDtyxGVew2d1qaoAVaBPEZH2tguhRIN06o";
    public static final String CLIENT_SECRET = "U262vjwP1CVe4YKqioOUR53TRgaEwsOeWPJOth21";
    public static final String TOKEN_SHARED_PREF = "token";
    public static final String URL_GENERATE_TOKEN = API_URL + "oauth/token";
    public static final String GRANT_TYPE = "ic_password";
    public static final String CLIENT_ID = "4";
    public static final UUID RFIDUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final Locale locale = new Locale( "id" , "ID" );

    public static final String LOGIN_PREFS = "LoginPrefs";

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateNow = dateFormat.format(date);

        return dateNow;
    }

    public static String getLinkImage(){
        return "http://dev.sisca.id/images/asset/";
    }
}

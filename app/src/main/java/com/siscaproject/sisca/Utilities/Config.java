package com.siscaproject.sisca.Utilities;

import java.util.Locale;
import java.util.UUID;

public class Config {
    public static final String API_URL = "http://10.132.5.28:8000/sisca/public/api/v1/";

    //public final static String CLIENT_SECRET = "krrUUmbDtyxGVew2d1qaoAVaBPEZH2tguhRIN06o";
    public static final String CLIENT_SECRET = "U262vjwP1CVe4YKqioOUR53TRgaEwsOeWPJOth21";
    public static final String TOKEN_SHARED_PREF = "token";
    public static final String URL_GENERATE_TOKEN = API_URL + "oauth/token";
    public static final String GRANT_TYPE = "password";
    public static final String CLIENT_ID = "4";
    public static final UUID RFIDUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final Locale locale = new Locale( "id" , "ID" );
}

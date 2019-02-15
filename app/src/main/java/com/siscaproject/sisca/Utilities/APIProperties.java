package com.siscaproject.sisca.Utilities;

import com.pixplicity.easyprefs.library.Prefs;

public class APIProperties {
    public static final String LOGIN_URL = Config.API_URL + "login";
    public static final String USER_URL = Config.API_URL + "users";
    public static final String ASSET_FIXED_URL = Config.API_URL + "asset/fixed";
    public static final String ASSET_IT_URL = Config.API_URL + "asset/it";
    public static final String ASSET_STOCK = Config.API_URL + "asset/stock";
    public static final String SETTING_CATEGORY_URL = Config.API_URL + "setting/category";
    public static final String SETTING_COMPANY_URL = Config.API_URL + "setting/company";
    public static final String SETTING_LABEL_URL = Config.API_URL + "setting/label";
    public static final String SETTING_MODEL_URL = Config.API_URL + "setting/model";
    public static final String SETTING_LOCATION_URL = Config.API_URL + "setting/location";
    public static final String SETTING_SUPPLIER_URL = Config.API_URL + "setting/supplier";
    public static final String SETTING_MANUFACTURER_URL = Config.API_URL + "setting/manufacturer";
    public static final String SETTING_DEPRECIATION_URL = Config.API_URL + "setting/depreciation";

    public String auth = Prefs.getString("token_type", "null")
            + " " + Prefs.getString("access_token", "null");
    public String accept = "application/json";

    public static UserService getUserService(){
        return RetrofitClient.getClient(Config.API_URL).create(UserService.class);
    }
}

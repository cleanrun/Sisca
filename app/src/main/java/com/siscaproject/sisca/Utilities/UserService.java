package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.Location;
import com.siscaproject.sisca.Model.LoginAuth;
import com.siscaproject.sisca.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    // Login Call ----------------------------------------------------------------------------------
    @FormUrlEncoded
    @POST("login")
    Call<LoginAuth> getLogin(@Field("email") String email, @Field("password") String password);




}

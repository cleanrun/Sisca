package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.AssetStock;
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

    // User Call -----------------------------------------------------------------------------------
    @GET("users")
    Call<User> indexUser();

    @FormUrlEncoded
    @POST("users")
    Call<User> storeUser(@Body User user);

    @GET("users/{user}")
    Call<User> showUser();

    @PUT("users/{user}")
    Call<User> putUser(@Body User user);

    @DELETE("users/{user}")
    Call<User> deleteUser(@Query("id") String id);

    // Asset Fixed Calls ---------------------------------------------------------------------------
    //@GET("asset/fixed")
    //Call<ResponseIndex<Asset>> indexFixed(@Header("Authorization") String auth, @Header("Accept") String accept);

    //@FormUrlEncoded
    @POST("asset/fixed")
    Call<Asset> storeFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Body Asset asset);

    @GET("asset/fixed/{fixed}")
    Call<Asset> showFixed();

    @PUT("asset/fixed/{fixed}")
    Call<Asset> putFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("fixed") int id, @Body Asset asset);

    //@DELETE("asset/fixed/{fixed}")
    //Call<ResponseDelete> deleteFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("fixed") int id);

    // Asset IT Calls ------------------------------------------------------------------------------
    @GET("asset/it")
    Call<Asset> indexIT();

    @FormUrlEncoded
    @POST("asset/it")
    Call<Asset> storeIT(@Body Asset asset);

    @GET("asset/it/{it}")
    Call<Asset> showIT();

    @PUT("asset/it/{it}")
    Call<Asset> putIT(@Body Asset asset);

    @DELETE("asset/it/{it}")
    Call<Asset> deleteIT(@Query("id") String id);

    // Asset Stock Calls ---------------------------------------------------------------------------
    //@GET("asset/stock")
    //Call<ResponseIndex<Asset>> indexStock(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("asset/stock")
    Call<AssetStock> storeStock(@Body AssetStock asset);

    @GET("asset/stock/{stock}")
    Call<AssetStock> showStock();

    @PUT("asset/stock/{stock}")
    Call<AssetStock> putStock(@Body AssetStock asset);

    @DELETE("asset/stock/{stock}")
    Call<AssetStock> deleteStock(@Query("id") String id);


}

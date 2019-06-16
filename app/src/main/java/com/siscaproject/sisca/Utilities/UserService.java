package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.AssetModel;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.LoginAuth;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {
    @FormUrlEncoded
    @POST("login")
    Call<LoginAuth> getLogin(@Field("email") String email, @Field("password") String password);

    @GET("users")
    Call<ResponseIndex<User>> indexUser(@Header("Authorization") String auth, @Header("Accept") String accept);

    @GET("asset")
    Call<ResponseIndex<AssetAPI>> indexAsset(@Header("Authorization") String auth, @Header("Accept") String accept);

    @GET("asset")
    Call<ResponseIndex<AssetMutasi>> indexAssetMutasi(@Header("Authorization") String auth, @Header("Accept") String accept);

    @GET("location")
    Call<ResponseIndex<LocationAPI>> indexLocation(@Header("Authorization") String auth, @Header("Accept") String accept);

    @GET("asset/fixed")
    Call<ResponseIndex<Asset>> indexFixed(@Header("Authorization") String auth, @Header("Accept") String accept);


}

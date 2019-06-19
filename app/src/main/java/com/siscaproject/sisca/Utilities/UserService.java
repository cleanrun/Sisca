package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.LoginAuth;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.ResponseShow;
import com.siscaproject.sisca.Model.ResponseStore;
import com.siscaproject.sisca.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @FormUrlEncoded
    @POST("login")
    Call<LoginAuth> getLogin(@Field("email") String email, @Field("password") String password);

    // index Calls ---------------------------------------------------------------------------------

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

    // show Calls ----------------------------------------------------------------------------------

    @GET("asset/{asset}")
    Call<ResponseShow<AssetMutasi>> showAset(@Path("asset") String id, @Header("Authorization") String auth, @Header("Accept") String accept);

    // store Calls ---------------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("mutation")
    Call<ResponseStore> storeMutation(@Header("Authorization") String auth, @Header("Accept") String accept,
                                      @Field("asset_id") int asset_id, @Field("description") String description,
                                      @Field("new_location_id") int new_location_id, @Field("new_pic_id") int new_pic_id,
                                      @Field("reason") String reason);

    // put Calls -----------------------------------------------------------------------------------

    @PUT("asset/{asset}")
    Call<AssetAPI> putAsset(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("asset") int id, @Body AssetAPI assetAPI);

    @PUT("location/{location}")
    Call<LocationAPI> putLocation(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("location") int id, @Body LocationAPI locationAPI);
}

package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.AssetStock;
import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.Model.Company;
import com.siscaproject.sisca.Model.Depreciation;
import com.siscaproject.sisca.Model.Label;
import com.siscaproject.sisca.Model.Location;
import com.siscaproject.sisca.Model.LoginAuth;
import com.siscaproject.sisca.Model.Manufacturer;
import com.siscaproject.sisca.Model.Model;
import com.siscaproject.sisca.Model.ResponseDelete;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.Supplier;
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
    @GET("asset/fixed")
    Call<ResponseIndex<Asset>> indexFixed(@Header("Authorization") String auth, @Header("Accept") String accept);

    //@FormUrlEncoded
    @POST("asset/fixed")
    Call<Asset> storeFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Body Asset asset);

    @GET("asset/fixed/{fixed}")
    Call<Asset> showFixed();

    @PUT("asset/fixed/{fixed}")
    Call<Asset> putFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("fixed") int id, @Body Asset asset);

    @DELETE("asset/fixed/{fixed}")
    Call<ResponseDelete> deleteFixed(@Header("Authorization") String auth, @Header("Accept") String accept, @Path("fixed") int id);

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
    @GET("asset/stock")
    Call<ResponseIndex<Asset>> indexStock(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("asset/stock")
    Call<AssetStock> storeStock(@Body AssetStock asset);

    @GET("asset/stock/{stock}")
    Call<AssetStock> showStock();

    @PUT("asset/stock/{stock}")
    Call<AssetStock> putStock(@Body AssetStock asset);

    @DELETE("asset/stock/{stock}")
    Call<AssetStock> deleteStock(@Query("id") String id);

    // Category Calls ------------------------------------------------------------------------------
    @GET("setting/category")
    Call<ResponseIndex<Category>> indexCategory(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/category")
    Call<Category> storeCategory(@Body Category category);

    @GET("setting/category/{category}")
    Call<Category> showCategory();

    @PUT("setting/category/{category}")
    Call<Category> putCategory(@Body Category category);

    @DELETE("setting/category/{category}")
    Call<Category> deleteCategory(@Query("id") String id);

    // Company Calls -------------------------------------------------------------------------------
    @GET("setting/company")
    Call<ResponseIndex<Company>> indexCompany(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/company")
    Call<Company> storeCompany(@Body Company company);

    @GET("setting/company/{company}")
    Call<Company> showCompany();

    @PUT("setting/company/{company}")
    Call<Company> putCompany(@Body Company company);

    @DELETE("setting/company/{company}")
    Call<Company> deleteCompany(@Query("id") String id);

    // Label Calls ---------------------------------------------------------------------------------
    @GET("setting/label")
    Call<ResponseIndex<Label>> indexLabel(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/label")
    Call<Label> storeLabel(@Header("Authorization") String auth, @Header("Accept") String accept, @Body Label label);

    @GET("setting/label/{label}")
    Call<Label> showLabel();

    @PUT("setting/label/{label}")
    Call<Label> putLabel(@Body Label label);

    @DELETE("setting/label/{label}")
    Call<Label> deleteLabel(@Query("id") String id);

    // Model Calls ---------------------------------------------------------------------------------
    @GET("setting/model")
    Call<ResponseIndex<Model>> indexModel(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/model")
    Call<Model> storeModel(@Body Model model);

    @GET("setting/model/{model}")
    Call<Model> showModel();

    @PUT("setting/model/{model}")
    Call<Model> putModel(@Body Model model);

    @DELETE("setting/model/{model}")
    Call<Model> deleteModel(@Query("id") String id);

    // Location Calls ------------------------------------------------------------------------------
    @GET("setting/location")
    Call<ResponseIndex<Location>> indexLocation(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/location")
    Call<Location> storeLocation(@Body Location location);

    @GET("setting/location/{location}")
    Call<Location> showLocation();

    @PUT("setting/location/{location}")
    Call<Location> putLocation(@Body Location location);

    @DELETE("setting/location/{location}")
    Call<Location> deleteLocation(@Query("id") String id);

    // Supplier Calls ------------------------------------------------------------------------------
    @GET("setting/supplier")
    Call<ResponseIndex<Supplier>> indexSupplier(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/supplier")
    Call<Supplier> storeSupplier(@Body Supplier supplier);

    @GET("setting/supplier/{supplier}")
    Call<Supplier> showSupplier();

    @PUT("setting/supplier/{supplier}")
    Call<Supplier> putSupplier(@Body Supplier supplier);

    @DELETE("setting/supplier/{supplier}")
    Call<Supplier> deleteSupplier(@Query("id") String id);

    // Manufacturer Calls --------------------------------------------------------------------------
    @GET("setting/manufacturer")
    Call<ResponseIndex<Manufacturer>> indexManufacturer(@Header("Authorization") String auth, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("setting/manufacturer")
    Call<Manufacturer> storeManufacturer(@Body Manufacturer manufacturer);

    @GET("setting/manufacturer/{manufacturer}")
    Call<Manufacturer> showManufacturer();

    @PUT("setting/manufacturer/{manufacturer}")
    Call<Manufacturer> putManufacturer(@Body Manufacturer manufacturer);

    @DELETE("setting/manufacturer/{manufacturer}")
    Call<Manufacturer> deleteManufacturer(@Query("id") String id);

    // Depreciation Calls --------------------------------------------------------------------------
    @GET("setting/depreciation")
    Call<Depreciation> indexDepreciation();

    @FormUrlEncoded
    @POST("setting/depreciation")
    Call<Depreciation> storeDepreciation(@Body Manufacturer manufacturer);

    @GET("setting/depreciation/{depreciation}")
    Call<Depreciation> showDepreciation();

    @PUT("setting/depreciation/{depreciation}")
    Call<Depreciation> putDepreciation(@Body Manufacturer manufacturer);

    @DELETE("setting/depreciation/{depreciation}")
    Call<Depreciation> deleteDepreciation(@Query("id") String id);

}

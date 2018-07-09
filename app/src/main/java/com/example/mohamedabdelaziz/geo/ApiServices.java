package com.example.mohamedabdelaziz.geo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiServices {
    @FormUrlEncoded
    @POST("user_login.php")
    Call<LoginResponse> user_login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("comp_login.php")
    Call<LoginResponse> comp_login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("medical_rep_login.php")
    Call<LoginResponse> medical_login(@Field("username") String username, @Field("password") String password);


    @FormUrlEncoded
    @POST("user_register.php")
    Call<LoginResponse> user_register(@Field("username") String username, @Field("fullname") String fullname, @Field("email") String email,
                                      @Field("password") String password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("comp_register.php")
    Call<LoginResponse> company_register(@Field("username") String username, @Field("telephone") String fullname, @Field("email") String email,
                                         @Field("password") String password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("medical_rep_register.php")
    Call<LoginResponse> medical_register(@Field("username") String username, @Field("telephone") String fullname, @Field("email") String email,
                                         @Field("password") String password, @Field("confirm_password") String confirm_password);

    @GET("get_products.php")
    Call<ProductsModel> getProducts();


    //////// order the product from the company
    @GET
    Call<LoginResponse> requestOrder(@Url String url);

    /////// my orders as a user
    @GET
    Call<MyOrdersResponse> getMyorders(@Url String url);

    ////get the medical rep orders
    @GET
    Call<OrdersResponse> medicalrepOrders(@Url String url);

    ////get the medical rep tasks
    @GET
    Call<TasksResponse> medicalrepTasks(@Url String url);

    ////get the hospitals
    @GET
    Call<HospitalsResponse> medicalrepHospitals(@Url String url);

    ////get the clinics
    @GET
    Call<ClinicsResponse> medicalrepClinics(@Url String url);

    ////get the pharmacies
    @GET
    Call<PharmacyResponse> medicalrepPharmacies(@Url String url);

    @GET("request_departments.php")
    Call<GovernatesResponse> requestGovernates();

    @GET
    Call<GovernatesResponse> requestCountry(@Url String url);

    ////get orders as a company
    @GET
    Call<CompanyOrdersResponse> companyOrders(@Url String url);

    ////get products as a company
    @GET
    Call<ProductsModel> companyProduct(@Url String url);

    ////get sales as a company
    @GET
    Call<SalesResponse> companySales(@Url String url);

    ////get sales as a company***************************
    @GET
    Call<FeedBackResponse> companyFeedback(@Url String url);

    ///get company medical resp
    @GET
    Call<MedicalRepsResponse> getMyMedReps(@Url String url);

    ////redirect product to med rep
    @GET
    Call<MessageResponse> redirectProduct(@Url String url);

    ////get company medicalresp
    @GET
    Call<MedicalRepsResponse> getMyMembers(@Url String url);

    ////get company users
    @GET("get_my_members.php")
    Call<UsersResponse> getUsers();

    ///company add medical rep
    @FormUrlEncoded
    @POST("add_member.php")
    Call<MessageResponse> add_member(@Field("username") String username, @Field("password") String pass, @Field("email") String email,
                                     @Field("telephone") String phone, @Field("copmany_id") String copmany_id);

    ///company add user
    @FormUrlEncoded
    @POST("add_user.php")
    Call<MessageResponse> add_user(@Field("username") String username, @Field("password") String pass, @Field("email") String email,
                                   @Field("telephone") String phone);

    ////delete company medicalresp
    @GET
    Call<MessageResponse> deleteMyMedicals(@Url String url);

    ///company add product
    @FormUrlEncoded
    @POST("add_product.php")
    Call<MessageResponse> add_product(@Field("name") String name, @Field("description") String description,
                                      @Field("percautions") String percautions, @Field("price") String price,
                                      @Field("image") String image, @Field("image_name") String image_name, @Field("company_id") String company_id);

    @FormUrlEncoded
    @POST("update_product.php")
    Call<MessageResponse> update_product(@Field("name") String name, @Field("description") String description,
                                         @Field("percautions") String percautions, @Field("price") String price,
                                         @Field("image") String image, @Field("image_name") String image_name,
                                         @Field("company_id") String company_id, @Field("updated") int updated, @Field("product_id") String product_id);

    ///////user delete order
    @GET
    Call<MessageResponse> deleteMyOrder(@Url String url);

    ////company delete product
    @GET
    Call<UsersResponse> deleteProduct(@Url String url);

    ///////order deliverd
    @GET
    Call<MessageResponse> orderDeliverd(@Url String url);

    @GET
    Call<MessageResponse> SetOrder(@Url String url);


    @GET
    Call<MessageResponse> DelteComment(@Url String url);
}

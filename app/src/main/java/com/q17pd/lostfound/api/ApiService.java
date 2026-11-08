package com.q17pd.lostfound.api;

import com.q17pd.lostfound.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("users")
    Call<StatusResponse> register(@Body Requests.Register request);
    // Я ебал это все пока сервер не умеет отдавать одного пользователя, поэтому приложение берет всех и ищет нужного.
    // TODO: заменить на GET users/{id}, когда он появится на сервере.
    @GET("users")
    Call<List<User>> getUsers();
    @PUT("users/{id}")
    Call<StatusResponse> editName(@Path("id") int id, @Body Requests.EditName request);
    @PUT("users/{id}/additional-info")
    Call<StatusResponse> editContact(@Path("id") int id, @Body Requests.EditContact request);
    @PUT("users/{id}/password")
    Call<StatusResponse> editPassword(@Path("id") int id, @Body Requests.EditPassword request);
    @Multipart
    @PUT("users/{id}/avatar")
    Call<StatusResponse> editAvatar(@Path("id") int id, @Part MultipartBody.Part image);
}

package com.droidcon.snaphack.service;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface LoginService {
    @POST("/src/webservice/login.php")
    void login(@Body LoginRequest loginRequest, Callback<LoginResponse> callback);
}

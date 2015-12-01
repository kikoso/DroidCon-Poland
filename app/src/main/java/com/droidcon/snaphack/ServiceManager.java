package com.droidcon.snaphack;

import android.content.Context;
import android.util.Log;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ServiceManager {
    private static final String TAG = "Service";
    private LoginService restService;
    private Context context;

    public ServiceManager(Context context) {
        this.context = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://52.32.159.250")
                .build();
        restService = restAdapter.create(LoginService.class);
    }

    public void login(String username, char[] password, final Callback<LoginResponse> callback) {
        login(username, password.toString(), callback);
       // login(username, password.toString(), callback);
    }

    private void login(String username, String password, final Callback<LoginResponse> callback) {
        restService.login(new LoginRequest(username, password), new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                new KeyManager(context).save(loginResponse.getKey());
                callback.success(loginResponse, response);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getLocalizedMessage());
                callback.failure(error);
            }
        });
    }
}
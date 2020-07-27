package com.example.beckart.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.widget.Toast;

import com.example.beckart.net.RetrofitClient;
import com.example.beckart.view.AddProductActivity;

import java.io.IOException;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductRepository {

    private static final String TAG = AddProductRepository.class.getSimpleName();
    private Application application;

    public AddProductRepository(Application application) {
        this.application = application;
    }

    public LiveData<ResponseBody> addProduct(Map<String, RequestBody> productInfo, MultipartBody.Part image) {
        final MutableLiveData<ResponseBody> mutableLiveData = new MutableLiveData<>();
        Log.d("addProduct", "onResponse: " + "4");
        RetrofitClient.getInstance().getApi().insertProduct(productInfo,image).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("addProduct", "onResponse: " + "Product Inserted");

                ResponseBody responseBody = response.body();
                if (response.body() != null) {

                    try {
                        Log.d("addProduct", "onResponse: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   mutableLiveData.setValue(responseBody);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("addProduct", "onResponse: " + "7");
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mutableLiveData;
    }
}

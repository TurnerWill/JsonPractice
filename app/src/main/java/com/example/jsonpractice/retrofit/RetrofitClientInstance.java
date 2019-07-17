package com.example.jsonpractice.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jsonpractice.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance extends AppCompatActivity {

    // declare and init base url
    private static final String BASE_URL = "http:/shibe.online/api/";

    // declare retrofit object
    private static Retrofit retrofit;

    // create a private constructor to make class a Singleton
    private RetrofitClientInstance(){};

    // create static method to allow access to Singleton Retrofit object
    public static Retrofit getRetrofit(){
        if ( retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

package com.example.myapplication.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBase {

    companion object{
        const val EMULATED_LOCALHOST = "http://10.0.2.2:3000"
        //const val DEVICE_IP = "http://123.456.789.123:8080",
        //const val ACTUAL_URL = "https://whatever.com:some_port"
    }

    val retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(EMULATED_LOCALHOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val service by lazy {

        retrofit.create(ProductAPI::class.java)

    }

}
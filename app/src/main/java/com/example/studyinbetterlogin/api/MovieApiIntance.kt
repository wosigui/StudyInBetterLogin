package com.example.studyinbetterlogin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiInstance {
    //创建Retrofit2对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //创建网络请求接口对象
    val api:MovieApiInterface = retrofit.create(MovieApiInterface::class.java)
}
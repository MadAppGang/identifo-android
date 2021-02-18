package com.prytula.identifolib.extensions

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

inline fun <reified T> OkHttpClient.createWebService(url: String): T {
    val gson = GsonBuilder()
            .setLenient()
            .create()
    return Retrofit.Builder()
            .baseUrl(url)
            .client(this)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(T::class.java)
}
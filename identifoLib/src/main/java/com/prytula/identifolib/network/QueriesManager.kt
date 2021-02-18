package com.prytula.identifolib.network

import android.content.Context
import com.prytula.identifolib.IdentifoAuthInterceptor
import com.prytula.identifolib.MockRequestInterceptor
import com.prytula.identifolib.QueryManager
import com.prytula.identifolib.extensions.createWebService
import com.prytula.identifolib.network.queries.QueriesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class QueriesManager : QueryManager<QueriesService> {
    override fun getNetworkService(
        context: Context,
        baseUrl: String,
        appId: String,
        secretKey: String,
        token: String
    ): QueriesService {
        val identifoAuthInterceptor = IdentifoAuthInterceptor(appId, secretKey, token)
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val mockInterceptor = MockRequestInterceptor(context)
        return OkHttpClient.Builder()
            .addInterceptor(identifoAuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .build()
            .createWebService(baseUrl)
    }
}
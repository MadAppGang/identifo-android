package com.prytula.identifolib.network

import android.content.Context
import com.prytula.IdentifoAuth
import com.prytula.identifolib.IdentifoAuthInterceptor
import com.prytula.identifolib.MockRequestInterceptor
import com.prytula.identifolib.QueryManager
import com.prytula.identifolib.extensions.createWebService
import com.prytula.identifolib.network.queries.RefreshSessionQueries
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RefreshTokenManager : QueryManager<RefreshSessionQueries> {
    override fun getNetworkService(
        context: Context,
        baseUrl: String,
        appId: String,
        secretKey: String,
        token: String
    ): RefreshSessionQueries {
        val refreshIdentifoAuthInterceptor = IdentifoAuthInterceptor(
            appId,
            secretKey,
            token
        )
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val mockInterceptor = MockRequestInterceptor(context)
        return OkHttpClient.Builder()
            .addInterceptor(refreshIdentifoAuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .build()
            .createWebService(baseUrl)
    }
}
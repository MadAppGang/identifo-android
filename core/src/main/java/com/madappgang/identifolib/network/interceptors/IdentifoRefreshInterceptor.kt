package com.madappgang.identifolib.network.interceptors

import com.madappgang.identifolib.getPreparedDigest
import com.madappgang.identifolib.storages.ITokenDataStorage
import okhttp3.Interceptor
import okhttp3.Response


/*
 * Created by Eugene Prytula on 3/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoRefreshInterceptor(
    private val appID : String,
    private val appSecret : String,
    private val tokenStorage : ITokenDataStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBuilder = request.newBuilder()
            .header("X-Identifo-ClientID", appID)
            .header("Authorization", "Bearer ${tokenStorage.getTokens().refresh?.jwtEncoded}")
            .header("Digest", request.body.getPreparedDigest(appSecret))
            .build()

        return chain.proceed(requestBuilder)
    }
}
package com.prytula.identifolib.network.interceptors

import com.prytula.identifolib.getPreparedDigest
import okhttp3.Interceptor
import okhttp3.Response

/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoAuthInterceptor(
    private val appID : String,
    private val appSecret : String,
    private val bearerToken : String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBuilder = request.newBuilder()
                .header("X-Identifo-ClientID", appID)
                .header("Authorization", "Bearer $bearerToken")
                .header("Digest", request.body.getPreparedDigest(appSecret))
                .build()

        return chain.proceed(requestBuilder)
    }
}
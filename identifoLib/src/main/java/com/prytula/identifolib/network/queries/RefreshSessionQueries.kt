package com.prytula.identifolib.network.queries

import com.prytula.identifolib.BuildConfig
import com.prytula.identifolib.MockRequestInterceptor
import com.prytula.identifolib.entities.refreshToken.RefreshTokenDataSet
import com.prytula.identifolib.entities.refreshToken.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface RefreshSessionQueries {
    @Headers("${MockRequestInterceptor.MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/token")
    suspend fun refreshToken(@Body refreshTokenDataSet: RefreshTokenDataSet = RefreshTokenDataSet()) : RefreshTokenResponse
}
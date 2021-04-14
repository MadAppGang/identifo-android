package com.madappgang.identifolib.network

import com.madappgang.identifolib.entities.refreshToken.RefreshTokenDataSet
import com.madappgang.identifolib.entities.refreshToken.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface RefreshSessionQueries {
    @POST("/auth/token")
    fun refreshToken(
        @Body refreshTokenDataSet: RefreshTokenDataSet = RefreshTokenDataSet()
    ): Call<RefreshTokenResponse>
}
package com.prytula.identifolib.network

import com.prytula.identifolib.entities.refreshToken.RefreshTokenDataSet
import com.prytula.identifolib.entities.refreshToken.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface RefreshSessionQueries {
    @POST("/auth/token")
    suspend fun refreshToken(@Body refreshTokenDataSet: RefreshTokenDataSet = RefreshTokenDataSet()) : RefreshTokenResponse
}
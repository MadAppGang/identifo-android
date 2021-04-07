package com.madappgang.identifolib.entities.refreshToken

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RefreshTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)
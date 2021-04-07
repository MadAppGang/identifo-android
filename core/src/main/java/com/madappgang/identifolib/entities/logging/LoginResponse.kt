package com.madappgang.identifolib.entities.logging

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/12/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val loggedUser: LoggedUser
)
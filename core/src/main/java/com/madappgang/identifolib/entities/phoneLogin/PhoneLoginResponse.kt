package com.madappgang.identifolib.entities.phoneLogin

import com.google.gson.annotations.SerializedName
import com.madappgang.identifolib.entities.logging.LoggedUser


/*
 * Created by Eugene Prytula on 2/16/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneLoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val loggedUser: LoggedUser
)
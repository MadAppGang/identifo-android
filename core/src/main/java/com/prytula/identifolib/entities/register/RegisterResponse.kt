package com.prytula.identifolib.entities.register

import com.google.gson.annotations.SerializedName
import com.prytula.identifolib.entities.register.RegisteredUser


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RegisterResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("refresh_token") val refreshToken: String,
        @SerializedName("user") val registeredUser: RegisteredUser
)
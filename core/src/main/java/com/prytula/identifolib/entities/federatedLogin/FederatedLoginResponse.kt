package com.prytula.identifolib.entities.federatedLogin

import com.google.gson.annotations.SerializedName
import com.prytula.identifolib.entities.logging.LoggedUser


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class FederatedLoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val loggedUser: LoggedUser
)
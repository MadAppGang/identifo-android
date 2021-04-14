package com.madappgang.identifolib.entities.logout

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 3/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */


data class LogoutDataSet(
    @SerializedName("refresh_token") val refreshToken: String
)
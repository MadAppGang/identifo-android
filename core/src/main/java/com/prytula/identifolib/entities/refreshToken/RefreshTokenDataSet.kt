package com.prytula.identifolib.entities.refreshToken

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RefreshTokenDataSet(
    @SerializedName("scopes") val copes: List<String> = listOf("offline")
)
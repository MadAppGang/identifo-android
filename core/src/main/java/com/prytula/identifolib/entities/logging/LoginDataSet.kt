package com.prytula.identifolib.entities.logging

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/12/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class LoginDataSet(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("scopes") val scope: List<String> = listOf("offline", "chat")
)
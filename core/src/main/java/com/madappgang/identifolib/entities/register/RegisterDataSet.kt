package com.madappgang.identifolib.entities.register

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RegisterDataSet(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
        @SerializedName("scopes") val scope: List<String> = listOf("offline", "chat"),
        @SerializedName("anonymous") val anonymous : Boolean = false
)
package com.prytula.identifolib.entities.phoneLogin

import com.google.gson.annotations.SerializedName

/*
 * Created by Eugene Prytula on 2/16/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class PhoneLoginDataSet(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("code") val code: String,
    @SerializedName("scopes") val scope: List<String> = listOf("offline", "chat")
)
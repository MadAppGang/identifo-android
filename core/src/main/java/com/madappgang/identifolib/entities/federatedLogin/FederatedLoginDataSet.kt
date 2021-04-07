package com.madappgang.identifolib.entities.federatedLogin

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class FederatedLoginDataSet(
    @SerializedName("provider") val provider : String,
    @SerializedName("access_token") val accessToken : String,
    @SerializedName("register_if_new") val registerIfNew : Boolean = true,
    @SerializedName("scopes") val scope: List<String> = listOf("offline", "chat")
)
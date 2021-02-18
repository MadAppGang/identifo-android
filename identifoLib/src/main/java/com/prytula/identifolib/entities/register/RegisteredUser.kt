package com.prytula.identifolib.entities.register

import com.google.gson.annotations.SerializedName
import com.prytula.identifolib.entities.TfaInfo


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RegisteredUser(
        @SerializedName("id") val id: String,
        @SerializedName("username") val username: String,
        @SerializedName("email") val email: String,
        @SerializedName("tfa_info") val tfaInfo: TfaInfo
)
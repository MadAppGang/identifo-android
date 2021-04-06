package com.prytula.identifolib.entities.logging

import com.google.gson.annotations.SerializedName
import com.prytula.identifolib.entities.TfaInfo


/*
 * Created by Eugene Prytula on 2/12/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class LoggedUser(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("pswd") val password: String,
    @SerializedName("active") val active : String,
    @SerializedName("tfa_info") val tfaInfo: TfaInfo,
    @SerializedName("num_of_logins") val numberOfLogin : Int,
    @SerializedName("latest_login_time") val latestLoginTime : Int
)
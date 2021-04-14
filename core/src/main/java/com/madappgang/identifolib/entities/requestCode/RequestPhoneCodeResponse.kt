package com.madappgang.identifolib.entities.requestCode

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/16/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class RequestPhoneCodeResponse(
    @SerializedName("result") val result: String
)
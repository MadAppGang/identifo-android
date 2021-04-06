package com.prytula.identifolib.entities.reserPassword

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 3/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class ResetPasswordResponse(
    @SerializedName("result") val result: String
)
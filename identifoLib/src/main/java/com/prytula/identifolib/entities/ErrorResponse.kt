package com.prytula.identifolib.entities

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class ErrorResponse(
    @SerializedName("error") val error : Error
) : Throwable()
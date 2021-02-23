package com.prytula.identifolib.entities

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class Error(
    @SerializedName("id") val id : String = "",
    @SerializedName("message") val message : String = "",
    @SerializedName("detailed_message") val detailedMessage : String = "",
    @SerializedName("status") val status : Int = 0
)
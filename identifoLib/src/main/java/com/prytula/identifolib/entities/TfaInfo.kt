package com.prytula.identifolib.entities

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class TfaInfo(
        @SerializedName("is_enabled") val isEnabled: Boolean
)
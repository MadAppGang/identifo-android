package com.prytula.identifolib.entities.deanonymize

import com.google.gson.annotations.SerializedName


/*
 * Created by Eugene Prytula on 2/16/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class DeanonimizeDataSet(
    @SerializedName("old_username") val oldUsername: String,
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_username") val newUsername: String,
    @SerializedName("new_password") val newPassword: String
)
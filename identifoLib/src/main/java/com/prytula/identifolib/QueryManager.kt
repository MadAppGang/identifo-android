package com.prytula.identifolib

import android.content.Context


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface QueryManager<T> {
    fun getNetworkService(
        context: Context,
        baseUrl: String,
        appId: String,
        secretKey: String,
        token : String = ""
    ): T
}
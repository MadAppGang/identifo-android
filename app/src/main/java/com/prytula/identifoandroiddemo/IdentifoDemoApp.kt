package com.prytula.identifoandroiddemo

import android.app.Application
import com.prytula.IdentifoAuth


/*
 * Created by Eugene Prytula on 2/25/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appID = "bk9o707k3t4c72q2qqqq"
        val secret = "vUYvSt8rEI7lTPIM96MMwPS3"
        val baseUrl = "https://identifo.jackrudenko.com"

        IdentifoAuth.initAuthenticator(this, baseUrl, appID, secret)
    }
}
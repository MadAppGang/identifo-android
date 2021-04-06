package com.prytula.identifolibui.login.options

import java.io.Serializable


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class UseConditions(
    val userAgreement: String = "",
    val privacyPolicy: String = ""
) : Serializable
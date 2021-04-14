package com.madappgang.identifolibui.login.options

import java.io.Serializable


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

enum class LoginProviders(val isAuxiliaryIdentifier : Boolean) : Serializable {
    EMAIL(isAuxiliaryIdentifier = false),
    PHONE(isAuxiliaryIdentifier = false),
    GMAIL(isAuxiliaryIdentifier = true),
    FACEBOOK(isAuxiliaryIdentifier = true),
    TWITTER(isAuxiliaryIdentifier = true)
}
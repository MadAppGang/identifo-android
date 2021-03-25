package com.prytula.identifolibui.login.options

import java.io.Serializable


/*
 * Created by Eugene Prytula on 3/3/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class Style(
    val imageRes: Int? = null,
    val backgroundRes: Int? = null,
    val companyName: String = "",
    val greetingsText: String = ""
) : Serializable
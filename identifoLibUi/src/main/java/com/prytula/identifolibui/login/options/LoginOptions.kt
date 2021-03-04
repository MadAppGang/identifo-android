package com.prytula.identifolibui.login.options

import java.io.Serializable

/*
 * Created by Eugene Prytula on 3/3/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

// TODO: Migrate to parcelable
data class LoginOptions(
    val commonStyle: Style? = null,
    val providers : List<LoginProviders>
) : Serializable
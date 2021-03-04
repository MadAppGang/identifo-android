package com.prytula.identifolibui.login.options

import androidx.annotation.DrawableRes
import java.io.Serializable


/*
 * Created by Eugene Prytula on 3/3/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class Style(
    @DrawableRes val imageRes: Int? = null,
    @DrawableRes val backgroundRes : Int? = null
) : Serializable
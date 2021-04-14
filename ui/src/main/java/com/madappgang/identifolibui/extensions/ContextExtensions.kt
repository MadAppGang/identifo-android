package com.madappgang.identifolibui.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

internal fun Context.redirectToUrl(url : String) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(browserIntent)
    } catch (e : Exception) {
        // Ignore exceptions on purpose
    }
}
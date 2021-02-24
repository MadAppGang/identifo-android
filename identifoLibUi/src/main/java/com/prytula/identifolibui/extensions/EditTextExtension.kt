package com.prytula.identifolibui.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

internal inline fun EditText.onDone(crossinline callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback()
            true
        }
        false
    }
}
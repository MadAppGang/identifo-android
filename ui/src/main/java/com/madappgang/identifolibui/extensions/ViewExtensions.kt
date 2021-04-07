package com.madappgang.identifolibui.extensions

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */


internal fun View.showMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.addSystemTopPadding() {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        updatePadding(top = initialPadding.top + insets.systemWindowInsetTop)
        insets
    }
}

fun View.addSystemBottomPadding() {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
        insets
    }
}

fun View.addSystemTopBottomPadding() {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        updatePadding(
            top = initialPadding.top + insets.systemWindowInsetTop,
            bottom = initialPadding.bottom + insets.systemWindowInsetBottom
        )
        insets
    }
}

private fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

package com.madappgang.identifolibui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.extensions.startActivity
import com.madappgang.identifolibui.login.options.LoginOptions
import java.io.Serializable


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoSignInActivity : AppCompatActivity() {

    companion object {
        const val GOOGLE_APP_ID_KEY = "google_app_id_key"

        fun openActivity(context: Context, loginOptions: Serializable) {
            context.startActivity<IdentifoSignInActivity> {
                putExtra(GOOGLE_APP_ID_KEY, loginOptions)
            }
        }
    }

    val loginOptions: LoginOptions by lazy { intent.extras?.getSerializable(GOOGLE_APP_ID_KEY) as LoginOptions }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.decorView.rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
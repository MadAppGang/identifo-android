package com.prytula.identifolibui.login

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.startActivity
import com.prytula.identifolibui.login.options.LoginOptions
import java.io.Serializable


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoLoginActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_APP_ID_KEY = "google_app_id_key"

        fun openActivity(context: Context, loginOptions: Serializable) {
            context.startActivity<IdentifoLoginActivity> {
                putExtra(GOOGLE_APP_ID_KEY, loginOptions)
            }
        }
    }

    val loginOptions: LoginOptions by lazy { intent.extras?.getSerializable(GOOGLE_APP_ID_KEY) as LoginOptions }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_login)
    }
}
package com.prytula.identifolibui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.extensions.startActivity
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoRegistrationActivity : AppCompatActivity() {

    companion object {
        fun openActivity(context: Context) {
            context.startActivity<IdentifoRegistrationActivity>()
        }
    }

    private val rootView by lazy { findViewById<ConstraintLayout>(R.id.constraint_registration_root) }
    private val registerButton by lazy { findViewById<Button>(R.id.buttonRegister) }
    private val editTextUsername by lazy { findViewById<EditText>(R.id.editTextTextEmailAddress) }
    private val editTextPassword by lazy { findViewById<EditText>(R.id.editTextNumberPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_registration)

        registerButton.setOnClickListener {
            loginWithUsernameAndPassword(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
        }

        editTextPassword.onDone {
            loginWithUsernameAndPassword(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
        }
    }

    private fun loginWithUsernameAndPassword(username: String, password: String) {
        lifecycleScope.launch {
            IdentifoAuth.registerWithUsernameAndPassword(username, password, false).onSuccess {
                finish()
            }.onError {
                rootView.showMessage(it.error.message)
            }
        }
    }
}
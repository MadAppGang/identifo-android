package com.prytula.identifolibui.registration

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.ActivityIdentifoLoginBinding
import com.prytula.identifolibui.databinding.ActivityIdentifoRegistrationBinding
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

    private val registrationBinding by viewBinding(ActivityIdentifoRegistrationBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_registration)

        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }
    }

    private fun pushUsernameAndPassword() {
        val login = registrationBinding.editTextTextEmailAddress.text.toString()
        val password = registrationBinding.editTextPassword.text.toString()
        val repeatPassword = registrationBinding.editTextPasswordRepeat.text.toString()

        if (password == repeatPassword) {
            registerWithUsernameAndPassword(login, password)
        } else {
            registrationBinding.constraintRegistrationRoot.showMessage(getString(R.string.passwordsDoNotMatch))
        }
    }

    private fun registerWithUsernameAndPassword(username: String, password: String) {
        lifecycleScope.launch {
            IdentifoAuth.registerWithUsernameAndPassword(username, password, false).onSuccess {
                finish()
            }.onError {
                registrationBinding.constraintRegistrationRoot.showMessage(it.error.message)
            }
        }
    }
}
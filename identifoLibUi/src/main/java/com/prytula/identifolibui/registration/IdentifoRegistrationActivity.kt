package com.prytula.identifolibui.registration

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.ActivityIdentifoRegistrationBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.extensions.startActivity


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
    private val registrationViewModel by viewModels<IdentifoRegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_registration)

        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }

        registrationViewModel.registrationSuccessful.asLiveData()
            .observe(this) { registerResponse ->
                finish()
            }

        registrationViewModel.receiveError.asLiveData()
            .observe(this) { errorResponse ->
                registrationBinding.constraintRegistrationRoot.showMessage(errorResponse.error.message)
            }
    }

    private fun pushUsernameAndPassword() {
        val login = registrationBinding.editTextTextEmailAddress.text.toString()
        val password = registrationBinding.editTextPassword.text.toString()
        val repeatPassword = registrationBinding.editTextPasswordRepeat.text.toString()

        if (password == repeatPassword) {
            registrationViewModel.registerWithUsernameAndPassword(login, password)
        } else {
            registrationBinding.constraintRegistrationRoot.showMessage(getString(R.string.passwordsDoNotMatch))
        }
    }
}
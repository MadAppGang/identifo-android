package com.prytula.identifolibui.login.username

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentLoginUsernameBinding
import com.prytula.identifolibui.databinding.FragmentPhoneNumberLoginBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginFragment : Fragment(R.layout.fragment_login_username) {

    private val usernameLoginBinding by viewBinding(FragmentLoginUsernameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = usernameLoginBinding.editTextTextEmailAddress.text.toString()
        val password = usernameLoginBinding.editTextPassword.text.toString()

        usernameLoginBinding.editTextPassword.onDone { performLogin(username, password) }
        usernameLoginBinding.buttonLogin.setOnClickListener { performLogin(username, password) }
    }

    private fun performLogin(username: String, password: String) {
        lifecycleScope.launchWhenCreated {
            IdentifoAuth.loginWithUsernameAndPassword(username, password).onError {
                usernameLoginBinding.constraintLoginRoot.showMessage(it.error.message)
            }.onSuccess {
                requireActivity().finish()
            }
        }
    }
}
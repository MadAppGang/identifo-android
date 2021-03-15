package com.prytula.identifolibui.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentRegistrationBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val registrationBinding by viewBinding(FragmentRegistrationBinding::bind)
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }

        registrationViewModel.registrationSuccessful.asLiveData()
            .observe(viewLifecycleOwner) { registerResponse ->
                requireActivity().finish()
            }

        registrationViewModel.receiveError.asLiveData()
            .observe(viewLifecycleOwner) { errorResponse ->
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
package com.prytula.identifolibui.login.username

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentLoginUsernameBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.IdentifoActivity
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.Style


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginFragment : Fragment(R.layout.fragment_login_username) {

    private val usernameLoginBinding by viewBinding(FragmentLoginUsernameBinding::bind)
    private val usernameLoginViewModel: UsernameLoginViewModel by viewModels()

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoActivity).loginOptions }
    private val commonStyle: Style? by lazy { loginOptions.commonStyle }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonStyle?.let {
            usernameLoginBinding.imageViewLogo.load(it.companyLogo)
            usernameLoginBinding.textViewCompanyName.text = it.companyName
            usernameLoginBinding.textViewCompanyGreetings.text = it.greetingsText
        }

        usernameLoginBinding.editTextPassword.onDone {
            usernameLoginViewModel.performLogin(
                usernameLoginBinding.editTextEmailAddress.text.toString(),
                usernameLoginBinding.editTextPassword.text.toString()
            )
        }
        usernameLoginBinding.buttonLogin.setOnClickListener {
            usernameLoginViewModel.performLogin(
                usernameLoginBinding.editTextEmailAddress.text.toString(),
                usernameLoginBinding.editTextPassword.text.toString()
            )
        }

        usernameLoginViewModel.signInSuccessful.asLiveData().observe(viewLifecycleOwner) {
            requireActivity().finish()
        }

        usernameLoginViewModel.receiveError.asLiveData().observe(viewLifecycleOwner) {
            usernameLoginBinding.constraintLoginRoot.showMessage(it.error.message)
        }

        usernameLoginBinding.textViewRegisterNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_usernameLoginFragment_to_registrationFragment)
        }

        usernameLoginBinding.buttonRecoveryPassword.setOnClickListener {
            findNavController().navigate(R.id.action_usernameLoginFragment_to_resetPasswordFragment)
        }

        usernameLoginBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
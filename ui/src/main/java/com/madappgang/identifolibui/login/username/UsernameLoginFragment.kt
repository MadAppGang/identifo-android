package com.madappgang.identifolibui.login.username

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentLoginUsernameBinding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding
import com.madappgang.identifolibui.extensions.onDone
import com.madappgang.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginFragment : Fragment(R.layout.fragment_login_username) {

    private val usernameLoginBinding by viewBinding(FragmentLoginUsernameBinding::bind)
    private val usernameLoginViewModel: UsernameLoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameLoginBinding.nestedScrollViewLoginRoot.addSystemTopBottomPadding()

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

        usernameLoginViewModel.loginUIStates.asLiveData().observe(viewLifecycleOwner) { userNameLoginState ->
            when (userNameLoginState) {
                is UsernameLoginUIStates.LoginSuccessful -> closeSignInFlow()
                is UsernameLoginUIStates.LoginFailure -> showMessage(userNameLoginState.error.error.message)
                UsernameLoginUIStates.Loading -> showLoading()
                UsernameLoginUIStates.IDLE -> hideLoading()
            }
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

    private fun showMessage(message : String) {
        hideLoading()
        usernameLoginBinding.nestedScrollViewLoginRoot.showMessage(message)
    }

    private fun closeSignInFlow() {
        hideLoading()
        findNavController().navigate(R.id.action_usernameLoginFragment_pop_including_commonLoginFragment)
    }

    private fun showLoading() {
        usernameLoginBinding.progressBarLine.show()
        usernameLoginBinding.buttonLogin.isEnabled = false
    }

    private fun hideLoading() {
        usernameLoginBinding.progressBarLine.hide()
        usernameLoginBinding.buttonLogin.isEnabled = true
    }
}
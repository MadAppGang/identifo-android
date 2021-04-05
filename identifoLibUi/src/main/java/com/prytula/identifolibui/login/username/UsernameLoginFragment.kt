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
import com.prytula.identifolibui.extensions.addSystemTopBottomPadding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.IdentifoSignInActivity
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.Style


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginFragment : Fragment(R.layout.fragment_login_username) {

    private val usernameLoginBinding by viewBinding(FragmentLoginUsernameBinding::bind)
    private val usernameLoginViewModel: UsernameLoginViewModel by viewModels()

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoSignInActivity).loginOptions }
    private val commonStyle: Style? by lazy { loginOptions.commonStyle }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameLoginBinding.nestedScrollViewLoginRoot.addSystemTopBottomPadding()

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

        usernameLoginViewModel.loginUIStates.asLiveData().observe(viewLifecycleOwner) { userNameLoginState ->
            when (userNameLoginState) {
                is UsernameLoginUIStates.LoginSuccessful -> closeSignInFlow()
                is UsernameLoginUIStates.LoginFailure -> showMessage(userNameLoginState.error.error.message)
                UsernameLoginUIStates.Loading -> showLoading()
                UsernameLoginUIStates.IDLE -> hideLoading()
            }
        }

        usernameLoginBinding.textViewRegisterNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_usernameLoginFragment_to_navigation_graph_sign_up)
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
        requireActivity().finish()
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
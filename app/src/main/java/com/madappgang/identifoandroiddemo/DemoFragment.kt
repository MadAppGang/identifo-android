package com.madappgang.identifoandroiddemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifoandroiddemo.databinding.FragmentDemoBinding
import com.madappgang.identifolib.entities.AuthState
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolib.extensions.onSuccess
import com.madappgang.identifolibui.login.WelcomeLoginFragment
import com.madappgang.identifolibui.login.options.LoginOptions
import com.madappgang.identifolibui.login.options.LoginProviders
import com.madappgang.identifolibui.login.options.Style
import com.madappgang.identifolibui.login.options.UseConditions
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 4/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class DemoFragment : Fragment(R.layout.fragment_demo) {

    val binding by viewBinding(FragmentDemoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                IdentifoAuthentication.logout().onError { errorResponse ->
                    Snackbar.make(
                        binding.leanerLayoutRoot,
                        errorResponse.error.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }.onSuccess {
                    refreshAuthState()
                }
            }
        }

        refreshAuthState()

        binding.buttonSignIn.setOnClickListener {
            redirectToSignInFlow()
        }
    }

    private fun refreshAuthState() {
        IdentifoAuthentication.fetchAuthState { state: AuthState ->
            when (state) {
                is AuthState.Authentificated -> {
                    val accessToken = state.accessToken
                    val user = state.identifoUser
                    binding.textState.text = "User - ${user}, token - $accessToken"
                }
                else -> {
                    binding.textState.text = "Deauthenticated"
                }
            }
        }
    }

    private fun redirectToSignInFlow() {
        val style = Style(
            companyLogo = R.drawable.ic_madappgang,
            companyName = getString(R.string.company_name),
            greetingsText = getString(R.string.company_greetings)
        )

        val userConditions = UseConditions(
            "https://madappgang.com/",
            "https://madappgang.com/experience"
        )

        val providers = mutableListOf<LoginProviders>()

        if (binding.checkboxEmail.isChecked) providers += LoginProviders.EMAIL
        if (binding.checkboxPhoneNumber.isChecked) providers += LoginProviders.PHONE
        if (binding.checkboxGoogle.isChecked) providers += LoginProviders.GMAIL
        if (binding.checkboxFacebook.isChecked) providers += LoginProviders.FACEBOOK
        if (binding.checkboxTwitter.isChecked) providers += LoginProviders.TWITTER

        val loginOptions = LoginOptions(
            commonStyle = style,
            providers = providers,
            useConditions = userConditions
        )

        findNavController().navigate(
            R.id.action_demoFragment_to_navigation_graph_login,
            WelcomeLoginFragment.putArguments(loginOptions)
        )
    }
}
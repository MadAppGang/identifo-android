package com.prytula.identifolibui.login.phoneNumber.phoneNumberLogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentPhoneNumberLoginBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.IdentifoActivity
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.Style
import com.prytula.identifolibui.login.phoneNumber.oneTimePassword.OneTimePasswordFragment


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberFragment : Fragment(R.layout.fragment_phone_number_login) {

    private val phoneNumberLoginBinding by viewBinding(FragmentPhoneNumberLoginBinding::bind)

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoActivity).loginOptions }
    private val commonStyle: Style? by lazy { loginOptions.commonStyle }

    companion object {
        private val PHONE_NUMBER_PATTERN = "^[+][0-9]{11,13}\$".toRegex()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumberLoginBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        phoneNumberLoginBinding.textViewRegisterNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_phoneNumberLoginFragment_to_registrationFragment)
        }

        commonStyle?.let {
            phoneNumberLoginBinding.imageViewLogo.load(it.companyLogo)
            phoneNumberLoginBinding.textViewCompanyName.text = it.companyName
            phoneNumberLoginBinding.textViewCompanyGreetings.text = it.greetingsText
        }

        phoneNumberLoginBinding.textInputPhoneNumber.onDone {
            redirectToOtpReceivingScreen()
        }

        phoneNumberLoginBinding.buttonProceed.setOnClickListener {
            redirectToOtpReceivingScreen()
        }
    }

    private fun redirectToOtpReceivingScreen() {
        val phoneNumber = phoneNumberLoginBinding.textInputPhoneNumber.text.toString()
        val isNumberValid = phoneNumber.matches(PHONE_NUMBER_PATTERN)
        if (isNumberValid) {
            findNavController().navigate(
                R.id.action_phoneNumberLoginFragment_to_oneTimePasswordFragment,
                OneTimePasswordFragment.putArgument(phoneNumber)
            )
        } else {
            phoneNumberLoginBinding.constraintPhoneNumberRoot.showMessage(getString(R.string.phoneNumberInvalid))
        }
    }
}
package com.madappgang.identifolibui.login.phoneNumber.phoneNumberLogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentPhoneNumberLoginBinding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding
import com.madappgang.identifolibui.extensions.onDone
import com.madappgang.identifolibui.extensions.showMessage
import com.madappgang.identifolibui.login.phoneNumber.oneTimePassword.OneTimePasswordFragment


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberFragment : Fragment(R.layout.fragment_phone_number_login) {

    private val phoneNumberLoginBinding by viewBinding(FragmentPhoneNumberLoginBinding::bind)

    companion object {
        private val PHONE_NUMBER_PATTERN = "^[+][0-9]{11,13}\$".toRegex()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumberLoginBinding.nestedScrollViewPhoneNumberRoot.addSystemTopBottomPadding()

        phoneNumberLoginBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        phoneNumberLoginBinding.textViewRegisterNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_phoneNumberLoginFragment_to_registrationFragment)
        }

        phoneNumberLoginBinding.textInputPhoneNumber.onDone {
            redirectToOtpReceivingScreen()
        }

        phoneNumberLoginBinding.buttonProceed.setOnClickListener {
            redirectToOtpReceivingScreen()
        }
    }

    private fun redirectToOtpReceivingScreen() {
        phoneNumberLoginBinding.textInputLayoutFieldPhoneNumber.error = null
        val phoneNumber = phoneNumberLoginBinding.textInputPhoneNumber.text.toString()
        val isNumberValid = phoneNumber.matches(PHONE_NUMBER_PATTERN)
        if (isNumberValid) {
            findNavController().navigate(
                R.id.action_phoneNumberLoginFragment_to_oneTimePasswordFragment,
                OneTimePasswordFragment.putArgument(phoneNumber)
            )
        } else {
            phoneNumberLoginBinding.textInputLayoutFieldPhoneNumber.error = getString(R.string.phoneNumberInvalid)
        }
    }
}
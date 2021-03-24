package com.prytula.identifolibui.login.phoneNumber.phoneNumberLogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentPhoneNumberLoginBinding
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.phoneNumber.oneTimePassword.OneTimePasswordFragment


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberFragment : Fragment(R.layout.fragment_phone_number_login) {

    private val phoneNumberLoginBinding by viewBinding(FragmentPhoneNumberLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isPhoneNumberValid = false

        phoneNumberLoginBinding.countryCodePicker.run {
            registerCarrierNumberEditText(phoneNumberLoginBinding.textInputPhoneNumber)
            setPhoneNumberValidityChangeListener { isPhoneNumberValid = it }
        }

        phoneNumberLoginBinding.buttonProceed.setOnClickListener {
            if (isPhoneNumberValid) {
                val phoneNumber = phoneNumberLoginBinding.countryCodePicker.fullNumberWithPlus
                findNavController().navigate(
                    R.id.action_phoneNumberLoginFragment_to_oneTimePasswordFragment,
                    OneTimePasswordFragment.putArgument(phoneNumber)
                )
            } else {
                phoneNumberLoginBinding.constraintPhoneNumberRoot.showMessage(getString(R.string.phoneNumberInvalid))
            }
        }
    }
}
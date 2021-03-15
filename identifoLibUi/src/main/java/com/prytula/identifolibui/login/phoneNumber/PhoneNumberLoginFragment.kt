package com.prytula.identifolibui.login.phoneNumber

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import com.hbb20.CountryCodePicker
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentOneTimePasswordBinding
import com.prytula.identifolibui.databinding.FragmentPhoneNumberLoginBinding
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberLoginFragment : Fragment(R.layout.fragment_phone_number_login) {

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
                lifecycleScope.launchWhenCreated {
                    val phoneNumber = phoneNumberLoginBinding.countryCodePicker.fullNumberWithPlus
                    IdentifoAuth.requestPhoneCode(phoneNumber).onError {
                        phoneNumberLoginBinding.constraintPhoneNumberRoot.showMessage(it.error.message)
                    }.onSuccess {
                        findNavController().navigate(
                            R.id.action_phoneNumberLoginFragment_to_oneTimePasswordFragment,
                            OneTimePasswordFragment.putArgument(phoneNumber)
                        )
                    }
                }
            } else {
                phoneNumberLoginBinding.constraintPhoneNumberRoot.showMessage(getString(R.string.phoneNumberInvalid))
            }
        }
    }
}
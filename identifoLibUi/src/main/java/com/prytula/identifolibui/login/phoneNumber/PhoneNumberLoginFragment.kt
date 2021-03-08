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
import com.google.android.material.textfield.TextInputEditText
import com.hbb20.CountryCodePicker
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberLoginFragment : Fragment(R.layout.fragment_phone_number_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootView = view.findViewById<ConstraintLayout>(R.id.constraint_phone_number_root)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.textInputPhoneNumber)
        val numberPrefixView = view.findViewById<CountryCodePicker>(R.id.countryCodePicker)
        val buttonProceed = view.findViewById<Button>(R.id.buttonProceed)

        var isPhoneNumberValid = false

        numberPrefixView.registerCarrierNumberEditText(phoneNumberEditText)
        numberPrefixView.setPhoneNumberValidityChangeListener { isPhoneNumberValid = it }

        buttonProceed.setOnClickListener {
            if (isPhoneNumberValid) {
                lifecycleScope.launchWhenCreated {
                    val phoneNumber = numberPrefixView.fullNumberWithPlus
                    IdentifoAuth.requestPhoneCode(phoneNumber).onError {
                        rootView.showMessage(it.error.message)
                    }.onSuccess {
                        findNavController().navigate(
                            R.id.action_phoneNumberLoginFragment_to_oneTimePasswordFragment,
                            OneTimePasswordFragment.putArgument(phoneNumber)
                        )
                    }
                }
            } else {
                rootView.showMessage(getString(R.string.phoneNumberInvalid))
            }
        }
    }
}
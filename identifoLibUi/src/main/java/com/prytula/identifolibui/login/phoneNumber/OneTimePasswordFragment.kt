package com.prytula.identifolibui.login.phoneNumber

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.OnTextChangeListener
import com.prytula.identifolibui.OtpCodeView
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.hideSoftKeyboard
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.extensions.showSoftKeyboard


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class OneTimePasswordFragment : Fragment(R.layout.fragment_one_time_password) {

    companion object {
        private const val PHONE_NUMBER_KEY = "phone_number_key"
        fun putArgument(phoneNumber : String) = bundleOf(
            PHONE_NUMBER_KEY to phoneNumber
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = requireArguments().getString(PHONE_NUMBER_KEY) ?: ""
        val rootView = view.findViewById<ConstraintLayout>(R.id.constraint_otp_root)
        val otpCodeView = view.findViewById<OtpCodeView>(R.id.editTextOtp)

        otpCodeView.requestFocus()
        requireActivity().showSoftKeyboard()

        otpCodeView.setTextChangeListener(object : OnTextChangeListener {
            override fun textEntered(code: String) {
                requireActivity().hideSoftKeyboard()
                lifecycleScope.launchWhenCreated {
                    IdentifoAuth.phoneLogin(phoneNumber, code).onError {
                        rootView.showMessage(it.error.message)
                    }.onSuccess {
                        requireActivity().finish()
                    }
                }
            }
        })
    }
}
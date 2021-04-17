package com.madappgang.identifolibui.login.phoneNumber.oneTimePassword

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentOneTimePasswordBinding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding
import com.madappgang.identifolibui.extensions.hideSoftKeyboard
import com.madappgang.identifolibui.extensions.showMessage
import com.madappgang.identifolibui.extensions.showSoftKeyboard
import com.optview.otp.OnTextChangeListener
import java.util.concurrent.TimeUnit


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class OneTimePasswordFragment : Fragment(R.layout.fragment_one_time_password) {

    companion object {
        private const val PHONE_NUMBER_KEY = "phone_number_key"
        private const val SMS_CONSENT_REQUEST = 999
        fun putArgument(phoneNumber: String) = bundleOf(
            PHONE_NUMBER_KEY to phoneNumber
        )
    }

    private val regexPattern: Regex by lazy {
        """\d{6}""".toRegex()
    }

    private lateinit var smsVerificationReceiver: BroadcastReceiver

    private val oneTimePasswordBinding by viewBinding(FragmentOneTimePasswordBinding::bind)
    private val oneTimePasswordViewModel: OneTimePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTimePasswordBinding.constraintOtpRoot.addSystemTopBottomPadding()

        val phoneNumber = requireArguments().getString(PHONE_NUMBER_KEY) ?: ""

        oneTimePasswordViewModel.requestOtpCode(phoneNumber)
        oneTimePasswordBinding.textViewSentToPhoneNumber.text = String.format(getString(R.string.theCodeHasBeenSent), phoneNumber)

        oneTimePasswordBinding.editTextOtp.requestFocus()
        requireActivity().showSoftKeyboard()

        oneTimePasswordBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        oneTimePasswordBinding.editTextOtp.setTextChangeListener(object : OnTextChangeListener {
            override fun textEntered(code: String) {
                requireActivity().hideSoftKeyboard()
                oneTimePasswordViewModel.loginViaPhoneNumber(phoneNumber, code)
            }
        })
        oneTimePasswordBinding.editTextOtp.setOnClickListener {
            it.requestFocus()
            requireActivity().showSoftKeyboard()
        }
        oneTimePasswordBinding.textViewResendTheCode.setOnClickListener {
            oneTimePasswordViewModel.requestOtpCode(phoneNumber)
        }

        registerSMSReceiver()

        oneTimePasswordViewModel.oneTimePasswordUIState.asLiveData().observe(viewLifecycleOwner) { oneTimePasswordUIState ->
            when (oneTimePasswordUIState) {
                is OneTimePasswordUIStates.LoginSuccessful -> finishSignInFlow()
                is OneTimePasswordUIStates.LoginFailure -> showErrorMessage(oneTimePasswordUIState.error.error.message)
                OneTimePasswordUIStates.Loading -> showLoading()
                else -> hideLoading()
            }
        }

        oneTimePasswordViewModel.sendCodeUIState.asLiveData().observe(viewLifecycleOwner) { oneTimePasswordTimerState ->
            when (oneTimePasswordTimerState) {
                is OneTimePasswordTimerStates.TimerClick -> {
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(oneTimePasswordTimerState.timeLeft)
                    val time = DateUtils.formatElapsedTime(seconds)
                    oneTimePasswordBinding.textViewResentCountTimer.text = String.format(getString(R.string.resendTheCodeIn), time)
                    showTimer()
                }
                OneTimePasswordTimerStates.PossibleToSendCode -> hideTimer()
            }
        }
    }

    private fun finishSignInFlow() {
        hideLoading()
        findNavController().navigate(R.id.action_oneTimePasswordFragment_pop_including_commonLoginFragment)
    }

    private fun showErrorMessage(message : String) {
        hideLoading()
        oneTimePasswordBinding.constraintOtpRoot.showMessage(message)
    }

    private fun showLoading() {
        oneTimePasswordBinding.progressBarLine.show()
    }

    private fun hideLoading() {
        oneTimePasswordBinding.progressBarLine.hide()
    }

    private fun showTimer() {
        oneTimePasswordBinding.textViewResendTheCode.isVisible = false
        oneTimePasswordBinding.textViewResentCountTimer.isVisible = true
    }

    private fun hideTimer() {
        oneTimePasswordBinding.textViewResendTheCode.isVisible = true
        oneTimePasswordBinding.textViewResentCountTimer.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(smsVerificationReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    if (message.isNullOrBlank()) return
                    regexPattern.find(message)?.value?.let { otp ->
                        oneTimePasswordBinding.editTextOtp.setText(otp)
                    }
                }
        }
    }

    private fun registerSMSReceiver() {

        SmsRetriever.getClient(requireContext()).startSmsUserConsent(null)

        smsVerificationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                    val extras = intent.extras
                    val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                    when (smsRetrieverStatus.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val consentIntent =
                                extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                            try {
                                startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                            } catch (e: ActivityNotFoundException) {
                                e.localizedMessage
                            }
                        }
                    }
                }
            }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsVerificationReceiver, intentFilter)
    }
}
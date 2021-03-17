package com.prytula.identifolibui.resetPassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentResetPasswordBinding
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val resetPasswordBinding by viewBinding(FragmentResetPasswordBinding::bind)
    private val resetPasswordViewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPasswordViewModel.receiveError.asLiveData().observe(viewLifecycleOwner) { errorResponse ->
            resetPasswordBinding.constraintRoot.showMessage(errorResponse.error.message)
        }

        resetPasswordViewModel.passwordHasBeenReset.asLiveData().observe(viewLifecycleOwner) { resetPasswordResponse ->
            findNavController().popBackStack()
        }

        resetPasswordBinding.buttonResetPassword.setOnClickListener {
            val email = resetPasswordBinding.editTextResetPasswordEmail.text.toString()
            resetPasswordViewModel.resetPassword(email)
        }
    }
}
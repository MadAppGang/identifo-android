package com.madappgang.identifolibui.resetPassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentResetPasswordBinding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding
import com.madappgang.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val resetPasswordBinding by viewBinding(FragmentResetPasswordBinding::bind)
    private val resetPasswordViewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        resetPasswordBinding.constraintRoot.addSystemTopBottomPadding()

        resetPasswordViewModel.receiveError.asLiveData()
            .observe(viewLifecycleOwner) { errorResponse ->
                resetPasswordBinding.textInputLayoutFieldResetPasswordEmail.error = errorResponse.error.message
            }

        resetPasswordViewModel.passwordHasBeenReset.asLiveData()
            .observe(viewLifecycleOwner) { email ->
                resetPasswordBinding.textInputLayoutFieldResetPasswordEmail.error = null
                findNavController().navigate(
                    R.id.action_resetPasswordFragment_to_followTheLinkFragment,
                    FollowTheLinkFragment.putArguments(email)
                )
            }

        resetPasswordBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        resetPasswordBinding.buttonResetPassword.setOnClickListener {
            val email = resetPasswordBinding.editTextResetPasswordEmail.text.toString()
            resetPasswordViewModel.resetPassword(email)
        }
    }
}
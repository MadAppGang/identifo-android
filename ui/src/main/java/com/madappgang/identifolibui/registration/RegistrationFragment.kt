package com.madappgang.identifolibui.registration

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.madappgang.identifolib.entities.ErrorResponse
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentRegistrationBinding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding
import com.madappgang.identifolibui.extensions.onDone
import com.madappgang.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    companion object {
        private const val MIMETYPE_IMAGES = "image/*"
    }

    private val registrationBinding by viewBinding(FragmentRegistrationBinding::bind)
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                registrationBinding.imageViewAccount.load(it) {
                    crossfade(true)
                    placeholder(R.drawable.ic_account)
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationBinding.nestedScrollViewSignUpRoot.addSystemTopBottomPadding()

        registrationBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }


        registrationViewModel.registrationUIState.asLiveData()
            .observe(viewLifecycleOwner) { state ->
                when (state) {
                    RegistrationUIStates.Loading -> showLoading()
                    is RegistrationUIStates.RegistrationSuccessful -> closeActivity()
                    is RegistrationUIStates.RegistrationFailure -> showErrorMessage(state.error)
                    else -> hideLoading()
                }
            }

        registrationBinding.imageViewAccount.setOnClickListener {
            getImageContent.launch(MIMETYPE_IMAGES)
        }
    }

    private fun closeActivity() {
        hideLoading()
        findNavController().navigate(R.id.action_registrationFragment_pop_including_commonLoginFragment)
    }

    private fun showErrorMessage(errorResponse: ErrorResponse) {
        hideLoading()
        registrationBinding.textInputLayoutFieldConfirmPassword.error = errorResponse.error.detailedMessage
    }

    private fun showLoading() {
        registrationBinding.progressBarLine.show()
        registrationBinding.textInputLayoutFieldConfirmPassword.error = null
    }

    private fun hideLoading() {
        registrationBinding.progressBarLine.hide()
    }

    private fun pushUsernameAndPassword() {
        val login = registrationBinding.editTextUsername.text.toString()
        val password = registrationBinding.editTextPassword.text.toString()
        val repeatPassword = registrationBinding.editTextConfirmPassword.text.toString()

        registrationBinding.textInputLayoutFieldConfirmPassword.error = null
        if (password == repeatPassword) {
            registrationViewModel.registerWithUsernameAndPassword(login, password)
        } else {
            registrationBinding.textInputLayoutFieldConfirmPassword.error = getString(R.string.passwordsDoNotMatch)
        }
    }
}
package com.prytula.identifolibui.resetPassword

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentFollowTheLinkBinding
import com.prytula.identifolibui.extensions.addSystemTopBottomPadding


/*
 * Created by Eugene Prytula on 4/1/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class FollowTheLinkFragment : Fragment(R.layout.fragment_follow_the_link) {

    companion object {
        private const val EMAIL_KEY = "email_key"
        fun putArguments(email: String) = bundleOf(EMAIL_KEY to email)
    }

    private val followTheLinkBinding by viewBinding(FragmentFollowTheLinkBinding::bind)
    private val email: String by lazy { requireArguments().getString(EMAIL_KEY) ?: "" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followTheLinkBinding.root.addSystemTopBottomPadding()

        followTheLinkBinding.textViewFollowTheLink.text =
            String.format(getString(R.string.followTheLinkYouReceived), email)

        followTheLinkBinding.buttonBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_followTheLinkFragment_pop_including_resetPasswordFragment)
        }

        followTheLinkBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
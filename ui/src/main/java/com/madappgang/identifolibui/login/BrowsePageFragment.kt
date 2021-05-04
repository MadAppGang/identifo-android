package com.madappgang.identifolibui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.databinding.FragmentBrowsePageBinding
import com.madappgang.identifolibui.extensions.addSystemBottomPadding
import com.madappgang.identifolibui.extensions.addSystemTopBottomPadding


/*
 * Created by Eugene Prytula on 4/30/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class BrowsePageFragment : Fragment(R.layout.fragment_browse_page) {

    companion object {
        private const val BROWSE_PAGE_TITLE = "browse_page_title"
        private const val BROWSE_PAGE_URL = "browse_page_url"
        fun putArguments(
            title: String,
            url: String
        ) = bundleOf(
            BROWSE_PAGE_TITLE to title,
            BROWSE_PAGE_URL to url
        )
    }

    private val browsePageFragmentBinding by viewBinding(FragmentBrowsePageBinding::bind)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(BROWSE_PAGE_TITLE) ?: ""
        val url = arguments?.getString(BROWSE_PAGE_URL) ?: ""
        browsePageFragmentBinding.textViewPageTitle.text = title

        browsePageFragmentBinding.constraintBrowserRoot.addSystemTopBottomPadding()

        val chromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                val showProgress = newProgress != 100
                browsePageFragmentBinding.progressBarLine.isVisible = showProgress
            }
        }

        browsePageFragmentBinding.webView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = chromeClient
            webViewClient = WebViewClient()
            loadUrl(url)
        }

        browsePageFragmentBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        browsePageFragmentBinding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        browsePageFragmentBinding.webView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        browsePageFragmentBinding.webView.destroy()
    }
}
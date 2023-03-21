package com.luisenricke.helpyourself.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.databinding.FragmentPrivacyPolicyBinding
import java.util.Locale

class PrivacyPolicyFragment : BaseFragment() {

    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)

        getActivityContext().setBottomNavigationViewVisibility(false)

        // Toolbar
        getActivityContext().setSupportActionBar(binding.toolbar)
        setupActionBar(getActivityContext().supportActionBar, getString(R.string.settings_privacy_policy))

        binding.webview.apply {
            val language = Locale.getDefault().language

            val settings = settings
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            isVerticalScrollBarEnabled = true
            webViewClient = WebViewClient()

            if (language == "es") {
                loadUrl("file:///android_asset/privacy_policy-es.html")
            } else {
                loadUrl("file:///android_asset/privacy_policy-us.html")
            }

        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
                true
            }
            else              -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getActivityContext().setBottomNavigationViewVisibility(true)
        _binding = null
    }
}

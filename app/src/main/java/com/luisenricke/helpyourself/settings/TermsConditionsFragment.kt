package com.luisenricke.helpyourself.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.databinding.FragmentTermsConditionsBinding
import java.util.*

class TermsConditionsFragment : BaseFragment() {

    private var _binding: FragmentTermsConditionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTermsConditionsBinding.inflate(inflater, container, false)

        getActivityContext().setBottomNavigationViewVisibility(false)

        // Toolbar
        getActivityContext().setSupportActionBar(binding.toolbar)
        setupActionBar(getActivityContext().supportActionBar, getString(R.string.settings_terms_conditions))

        binding.webview.apply {
            val language = Locale.getDefault().language

            val settings = settings
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            isVerticalScrollBarEnabled = true
            webViewClient = WebViewClient()

            if (language == "es") {
                loadUrl("file:///android_asset/terms_conditions-es.html")
            } else {
                loadUrl("file:///android_asset/terms_conditions-us.html")
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

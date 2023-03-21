package com.luisenricke.helpyourself.alert

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luisenricke.androidext.intentBrowser
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.androidext.toastLong
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.entity.AlertContact
import com.luisenricke.helpyourself.databinding.FragmentAlertDetailBinding
import com.luisenricke.helpyourself.service.Connectivity
import com.luisenricke.helpyourself.service.SendSMS
import com.luisenricke.kotlinext.formatDateTimeExtended
import com.luisenricke.kotlinext.roundDecimals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AlertDetailFragment : BaseFragment() {

    private var _binding: FragmentAlertDetailBinding? = null
    private val binding get() = _binding!!

    private var idAlert: Long = 0L

    private lateinit var alertDetailAdapter: AlertDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = AlertDetailFragmentArgs.fromBundle(it)
            idAlert = args.idAlert
        }

        getActivityContext().setBottomNavigationViewVisibility(false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlertDetailBinding.inflate(inflater, container, false)

        alertDetailAdapter = setAlertDetailAdapter(binding.root.context)

        binding.apply {
            val context = root.context

            // Toolbar
            getActivityContext().setSupportActionBar(toolbar)
            setupActionBar(getActivityContext().supportActionBar, getString(R.string.alert_detail))

            // Alert
            val alert = database.alertDAO().get(idAlert)

            if (idAlert == 0L || alert == null) {
                toastLong(context.getString(R.string.alert_empty))
                navController.popBackStack()
            }

            // region BindingViews
            lblDate.text = alert!!.timestamp.time.formatDateTimeExtended()

            val latitude: Double = alert.latitude.roundDecimals(10).toDouble()
            val longitude: Double = alert.longitude.roundDecimals(10).toDouble()

            lblLocation.text = if (latitude != 0.0 && longitude != 0.0) {
                "$latitude, $longitude"
            } else {
                context.getString(R.string.alert_detail_location_not_available)
            }

            lblLocation.setOnClickListener {
                val maps = context.resources.getStringArray(R.array.settings_category_alert_map_list)

                val url = when (context.preferenceGet(Constraint.ALERT_MAPS, String::class) ?: maps[0]) {
                    maps[0] -> setOpenStreetMap(latitude, longitude)
                    maps[1] -> setGoogleMaps(latitude, longitude)
                    else    -> ""
                }

                if (url.isNotEmpty()) {
                    binding.root.context.intentBrowser(url)
                }
            }

            // WebView
            CoroutineScope(Dispatchers.IO).launch {
                val isConnect = Connectivity.getInstance(root.context).isNetworkAvailableSocket()

                withContext(Dispatchers.Main) {
                    webview.visibility = if (isConnect) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            webview.apply {
                val settings = settings
                // settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.javaScriptEnabled = true

                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false

                if (latitude != 0.0 && longitude != 0.0) {
                    visibility = View.VISIBLE
                    loadUrl(setMap(latitude, longitude))
                } else {
                    visibility = View.GONE
                }
            }

            // RecyclerView
            recyclerAlertContact.apply {
                setHasFixedSize(true)

                adapter = alertDetailAdapter
            }

            //  endregion BindingViews

            return binding.root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_alert_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
                true
            }
            R.id.menu_alert_delete -> {
                deleteAlert(this.getActivityContext())
                true
            }
            else                   -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        displayViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        getActivityContext().setBottomNavigationViewVisibility(true)
    }

    private fun deleteAlert(context: Context) {
        val alert = database.alertDAO().get(idAlert)
        Timber.v("Alert: ${alert.toString()}")

        MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.alert_delete_title))
                .setIcon(R.drawable.ic_baseline_report_problem_24)
                .setMessage(context.getString(R.string.alert_delete_message))
                .setPositiveButton(context.getString(R.string.alert_delete_positive)) { _, _ ->
                    alert?.let {
                        val isContactDeleted = database.alertDAO().delete(idAlert)
                        Timber.v("Alert with $isContactDeleted has been deleted with our child rows")
                        toastLong(context.getString(R.string.alert_deleted_successfully))
                        navController.popBackStack()
                    }
                }
                .setNegativeButton(context.getString(R.string.alert_delete_negative)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun setAlertDetailAdapter(context: Context): AlertDetailAdapter {
        val clickListener: (AlertContact) -> Unit = { item ->
            Timber.v("clickListener")
        }

        val longClickListener: (AlertContact) -> Unit = { item ->
            Timber.v("longClickListener")
        }

        return AlertDetailAdapter(context, idAlert, clickListener, longClickListener)
    }

    private fun displayViews() {
        val isAlertsEmpty = alertDetailAdapter.isEmpty()

        if (isAlertsEmpty) {
            binding.apply {
                recyclerAlertContact.visibility = View.GONE
                lblAlertContactsTitle.visibility = View.GONE
                layoutEmpty.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                recyclerAlertContact.visibility = View.VISIBLE
                lblAlertContactsTitle.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
            }

            alertDetailAdapter.update()
        }
    }

    private fun setMap(latitude: Double, longitude: Double): String {
        val latitudeFormat: Double = latitude.roundDecimals(10).toDouble()
        val longitudeFormat: Double = longitude.roundDecimals(10).toDouble()
        return "http://m.osmtools.de/?mlat=${latitudeFormat}&mlon=${longitudeFormat}&icon=4&zoom=15&iframe=1"
    }

    private fun setGoogleMaps(latitude: Double, longitude: Double): String {
        val latitudeFormat: Double = latitude.roundDecimals(SendSMS.LIMIT_DIGITS).toDouble()
        val longitudeFormat: Double = longitude.roundDecimals(SendSMS.LIMIT_DIGITS).toDouble()
        return "http://maps.google.com/?q=$latitudeFormat,$longitudeFormat"
    }

    private fun setOpenStreetMap(latitude: Double, longitude: Double): String {
        val latitudeFormat: Double = latitude.roundDecimals(SendSMS.LIMIT_DIGITS).toDouble()
        val longitudeFormat: Double = longitude.roundDecimals(SendSMS.LIMIT_DIGITS).toDouble()
        return "http://m.osmtools.de/?mlat=${latitudeFormat}&mlon=${longitudeFormat}&icon=4&zoom=19&iframe=1"
    }
}


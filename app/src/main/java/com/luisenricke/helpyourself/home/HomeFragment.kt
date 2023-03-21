package com.luisenricke.helpyourself.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.luisenricke.androidext.*
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.alert.AlertFacade
import com.luisenricke.helpyourself.databinding.FragmentHomeBinding
import com.luisenricke.helpyourself.service.LastLocation
import com.luisenricke.helpyourself.service.SensorForeground
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var isAlertServiceEnable: Boolean?
        get() = preferenceGet(Constraint.ALERT_SERVICE, Boolean::class)
        set(value) = preferenceSet(Constraint.ALERT_SERVICE, value!!)

    private var isQuickAlertActivated = false
    private var isQuickAlertSent = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.lblAlertMessage.text = "${binding.root.context.getString(R.string.home_quick_alert_content_active)} \n ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                binding.btnQuick.icon = binding.root.context.getDrawable(R.drawable.ic_baseline_report_problem_24)
                binding.btnQuick.text = binding.root.context.getString(R.string.home_quick_alert_button_deactivate)
                binding.lblAlertMessage.text = binding.root.context.getString(R.string.home_quick_alert_content_deactivate)
                isQuickAlertActivated = false

                if (isQuickAlertSent) {
                    requestAlertService2(binding.root.context)
                    isQuickAlertSent = false
                }
            }
        }

        binding.apply {

            btnQuick.setOnClickListener {
                if (!isQuickAlertActivated) {
                    isQuickAlertActivated = true
                    isQuickAlertSent = true
                    timer.start()
                    btnQuick.icon = root.context.getDrawable(R.drawable.ic_baseline_close_24)
                    btnQuick.text = root.context.getString(R.string.home_quick_alert_button_active)
                } else {
                    Timber.i("End")
                    isQuickAlertSent = false
                    timer.onFinish()
                    timer.cancel()
                }
            }

            bindAlert(this)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty()) {
            Timber.e(getString(R.string.on_request_permissions_result_grant_results))
            return
        }

        when (requestCode) {
            Constraint.PERMISSIONS_ALERT_SERVICE -> {
                val isGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

                if (isGranted) {
                    SensorForeground.startService(getActivityContext())

                    if (!LastLocation.getInstance(getActivityContext()).isGpsEnable) {
                        toastLong(getString(R.string.home_gps_request))
                    }
                } else {
                    Timber.e("${getString(R.string.on_request_permissions_result_denied_permissions)} ALERT")
                    alertStateView(isAlertServiceEnable!!, binding.btnAlert)
                }

                return
            }

            else                                 -> Timber.e(getString(R.string.on_request_permissions_result_doesnt_find_permission))
        }
    }

    private fun bindAlert(bind: FragmentHomeBinding) {
        alertStateView(isAlertServiceEnable!!, bind.btnAlert)

        bind.btnAlert.setOnClickListener {
            if (database.contactDAO().countHighlighted() <= 0) {
                toastLong(getString(R.string.home_contacts_highlighted_empty))
                return@setOnClickListener
            }

            val reverse = !isAlertServiceEnable!!
            isAlertServiceEnable = reverse
            alertStateView(reverse, bind.btnAlert)
        }
    }

    private fun requestAlertService(context: Context) {
        val permissions = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS)

        val message = PermissionMessage(
                context = getActivityContext(),
                title = getString(R.string.permission_message_alert_title),
                message = getString(R.string.permission_message_alert_message),
                positiveButton = getString(R.string.permission_message_alert_positive_button),
                negativeButton = getString(R.string.permission_message_alert_negative_button),
                denied = getString(R.string.permission_message_alert_denied)
        )

        if (checkPermissions(permissions)) {
            SensorForeground.startService(getActivityContext())

            if (!LastLocation.getInstance(context).isGpsEnable) {
                toastLong(getString(R.string.home_gps_request))
            }

        } else {
            requestCriticalPermissions(permissions, Constraint.PERMISSIONS_ALERT_SERVICE, message)
        }
    }

    private fun requestAlertService2(context: Context) {

        if (database.contactDAO().countHighlighted() <= 0) {
            toastLong(getString(R.string.home_contacts_highlighted_empty))
            return
        }

        val permissions = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS)

        val message = PermissionMessage(
                context = getActivityContext(),
                title = getString(R.string.permission_message_alert_title),
                message = getString(R.string.permission_message_alert_message),
                positiveButton = getString(R.string.permission_message_alert_positive_button),
                negativeButton = getString(R.string.permission_message_alert_negative_button),
                denied = getString(R.string.permission_message_alert_denied)
        )

        if (checkPermissions(permissions)) {
            val alertFacade = AlertFacade()
            alertFacade.alertTriggeredMainThreat(context)

            if (!LastLocation.getInstance(context).isGpsEnable) {
                toastLong(getString(R.string.home_gps_request))
            }

        } else {
            requestCriticalPermissions(permissions, Constraint.PERMISSIONS_ALERT_SERVICE, message)
        }
    }

    private fun alertStateView(isActive: Boolean, button: MaterialButton) {
        button.apply {
            if (isActive) {
                icon = getActivityContext().getDrawable(R.drawable.ic_baseline_record_voice_over_24)
                text = getString(R.string.alert_service_active)
                setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                requestAlertService(context)
            } else {
                icon = getActivityContext().getDrawable(R.drawable.ic_baseline_voice_over_off_24)
                text = getString(R.string.alert_service_deactivate)
                setBackgroundColor(Color.RED)
                SensorForeground.stopService(context)
            }
        }
    }
}

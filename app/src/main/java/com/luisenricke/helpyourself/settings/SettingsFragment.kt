package com.luisenricke.helpyourself.settings

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.androidext.preferenceSet
import com.luisenricke.androidext.toastLong
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.databinding.FragmentSettingsBinding
import com.luisenricke.helpyourself.service.SendSMS
import timber.log.Timber

class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // region PreferenceValues
    private var alertDefaultMessage: String?
        get() = preferenceGet(Constraint.ALERT_DEFAULT_MESSAGE, String::class)
        set(value) = preferenceSet(Constraint.ALERT_DEFAULT_MESSAGE, value!!)
    private var alertSensitivity: String?
        get() = preferenceGet(Constraint.ALERT_SENSITIVITY, String::class)
        set(value) = preferenceSet(Constraint.ALERT_SENSITIVITY, value!!)
    private var alertVibration: Boolean?
        get() = preferenceGet(Constraint.ALERT_VIBRATION, Boolean::class)
        set(value) = preferenceSet(Constraint.ALERT_VIBRATION, value!!)
    private var alertLocation: Boolean?
        get() = preferenceGet(Constraint.ALERT_LOCATION, Boolean::class)
        set(value) = preferenceSet(Constraint.ALERT_LOCATION, value!!)
    private var alertMaps: String?
        get() = preferenceGet(Constraint.ALERT_MAPS, String::class)
        set(value) = preferenceSet(Constraint.ALERT_MAPS, value!!)
    // endregion PreferenceValues

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            bindSensitivity(this)
            bindVibration(this)
            bindLocation(this)
            bindMap(this)
            bindDefaultMessage(this)

            layoutPrivacyPolicy.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsToPrivacyPolicy()
                navController.navigate(action)
            }

            layoutTermsConditions.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsToTermsConditions()
                navController.navigate(action)
            }

            layoutInformation.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsToInfo()
                navController.navigate(action)
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindSensitivity(bind: FragmentSettingsBinding) {
        Timber.v("Sensitivity: $alertSensitivity")
        bind.apply {
            val context = root.context
            val sensitivities = context.resources.getStringArray(R.array.settings_category_alert_sensitivity_list)
            val adapterSensitivity = ArrayAdapter(context, R.layout.item_sensitivity, sensitivities)

            with(lblItemsSensitivity) {
                setAdapter(adapterSensitivity)
                setOnItemClickListener { _, _, position, _ ->
                    lblSelectedSensitivity.text = sensitivities[position]
                    preferenceSet(Constraint.ALERT_SENSITIVITY, sensitivities[position])
                }
                layoutSensitivity.setOnClickListener { this.showDropDown() }
            }

            lblSelectedSensitivity.text = alertSensitivity
        }
    }

    private fun bindVibration(bind: FragmentSettingsBinding) {
        Timber.v("Vibration: $alertVibration")
        bind.apply {
            layoutVibration.setOnClickListener {
                val reverseChecked = !swVibration.isChecked
                swVibration.isChecked = reverseChecked
                alertVibration = reverseChecked
            }

            swVibration.setOnClickListener { alertVibration = swVibration.isChecked }

            swVibration.isChecked = alertVibration!!
        }
    }

    private fun bindLocation(bind: FragmentSettingsBinding) {
        Timber.v("Location: $alertLocation")
        bind.apply {
            layoutLocation.setOnClickListener {
                val reverseChecked = !swLocation.isChecked
                swLocation.isChecked = reverseChecked

            }

            swLocation.setOnClickListener {
                alertLocation = swLocation.isChecked
            }

            swLocation.isChecked = alertLocation!!
        }
    }

    private fun bindMap(bind: FragmentSettingsBinding) {
        Timber.v("Maps: $alertMaps")
        bind.apply {
            val context = root.context
            val maps = context.resources.getStringArray(R.array.settings_category_alert_map_list)
            val adapterMap = ArrayAdapter(context, R.layout.item_map, maps)

            with(lblItemsMap) {
                setAdapter(adapterMap)
                setOnItemClickListener { _, _, position, _ ->
                    lblSelectedMap.text = maps[position]
                    alertMaps = maps[position]
                }
                layoutMap.setOnClickListener { this.showDropDown() }
            }

            lblSelectedMap.text = alertMaps
        }
    }

    private fun bindDefaultMessage(bind: FragmentSettingsBinding) {
        Timber.v("Default message: $alertDefaultMessage")
        bind.apply {
            val context = root.context
            val smsMaxLength = SendSMS.getInstance(context).getMaxLength()
            layoutDefaultMessage.setOnClickListener {
                val defaultMessageParent = TextInputLayout(context)
                val defaultMessageChild = TextInputEditText(context)

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val density = context.resources.displayMetrics.density
                val padding = 16

                defaultMessageParent.setPadding((density * padding).toInt(), 0, (density * padding).toInt(), 0)
                defaultMessageParent.layoutParams = params
                defaultMessageParent.counterMaxLength = smsMaxLength
                defaultMessageParent.isCounterEnabled = true
                defaultMessageParent.boxBackgroundColor = Color.WHITE

                defaultMessageChild.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                defaultMessageChild.isVerticalScrollBarEnabled = true
                defaultMessageChild.background = null
                defaultMessageChild.isSingleLine = false
                defaultMessageChild.setLines(3)
                defaultMessageChild.maxLines = 5
                defaultMessageChild.gravity = Gravity.START or Gravity.TOP
                defaultMessageChild.setText(alertDefaultMessage)
                defaultMessageChild.requestFocus()

                defaultMessageParent.addView(defaultMessageChild)

                MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.settings_category_general_default_message))
                        .setIcon(R.drawable.ic_baseline_sms_24)
                        .setView(defaultMessageParent)
                        .setPositiveButton(context.getString(R.string.settings_category_general_default_message_positive_button)) { dialog, _ ->
                            if (defaultMessageChild.text?.isEmpty()!! || defaultMessageChild.text!!.length > smsMaxLength) {
                                toastLong(context.getString(R.string.settings_category_general_default_message_text_declining))
                            } else {
                                lblSelectedDefaultMessage.text = defaultMessageChild.text
                                alertDefaultMessage = defaultMessageChild.text.toString()
                                toastLong(context.getString(R.string.settings_category_general_default_message_text_successfully))
                            }

                            dialog.dismiss()
                        }
                        .setNegativeButton(context.getString(R.string.settings_category_general_default_message_negative_button)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
            }

            lblSelectedDefaultMessage.text = alertDefaultMessage
        }
    }
}

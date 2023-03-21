package com.luisenricke.helpyourself

import android.app.Application
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.androidext.preferenceSet
import com.luisenricke.helpyourself.database.AppDatabase
import timber.log.Timber

class App : Application() {

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initDefaultValues()
    }

    private fun initDefaultValues() {
        val alertMessage = preferenceGet(Constraint.ALERT_DEFAULT_MESSAGE, String::class)
        if (alertMessage == null) {
            preferenceSet(Constraint.ALERT_DEFAULT_MESSAGE, getString(R.string.alert_message_default))
        }

        val alertSensitivity = preferenceGet(Constraint.ALERT_SENSITIVITY, String::class)
        if (alertSensitivity == null) {
            val item = this.resources.getStringArray(R.array.settings_category_alert_sensitivity_list)[1]
            preferenceSet(Constraint.ALERT_SENSITIVITY, item)
        }

        val alertVibration = preferenceGet(Constraint.ALERT_VIBRATION, Boolean::class)
        if (alertVibration == null) {
            preferenceSet(Constraint.ALERT_VIBRATION, true)
        }

        val alertLocation = preferenceGet(Constraint.ALERT_LOCATION, Boolean::class)
        if (alertLocation == null) {
            preferenceSet(Constraint.ALERT_LOCATION, true)
        }

        val alertMaps = preferenceGet(Constraint.ALERT_MAPS, String::class)
        if (alertMaps == null) {
            val item = this.resources.getStringArray(R.array.settings_category_alert_map_list)[0]
            preferenceSet(Constraint.ALERT_MAPS, item)
        }

        val alertService = preferenceGet(Constraint.ALERT_SERVICE, Boolean::class)
        if (alertService == null) {
            preferenceSet(Constraint.ALERT_SERVICE, false)
        }
    }

    private fun initTimber() {

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}

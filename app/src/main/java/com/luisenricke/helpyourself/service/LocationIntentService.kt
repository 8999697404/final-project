package com.luisenricke.helpyourself.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.luisenricke.androidext.checkPermission
import com.luisenricke.helpyourself.R
import timber.log.Timber

// https://stackoverflow.com/questions/6775257/android-location-providers-gps-or-network-provider
// https://developerlife.com/2010/10/20/gps/
// TODO: make upgrading location with time
@Suppress("unused")
class LocationIntentService : IntentService("LocationIntentService") {

    companion object {
        private const val MIN_TIME: Long = 1000L * 60L * 1L // milliseconds * seconds * minutes
        private const val MIN_DISTANCE: Float = 10f // meters

        @JvmStatic
        fun startService(context: Context) {
            val intent = Intent(context, LocationIntentService::class.java)
            context.startService(intent)
        }
    }

    //    private lateinit var context: Context

    private val manager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var bestProvider: String? = null

    private val isPermissionEnable: Boolean
        get() = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    private var listener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreate() {
        super.onCreate()
        // manager.checkProviders(this)
    }

    //    override fun onStart(intent: Intent?, startId: Int) {
    //        //super.onStart(intent, startId)
    //        onHandleIntent(intent!!)
    //        stopSelf()
    //    }

    override fun onHandleIntent(intent: Intent?) {
        val location: Location? = getLocation()
        Timber.i("lat: ${location?.latitude}, lon: ${location?.longitude}")
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        //        removeUpdate() // FIX: Check if get location with this
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(): Location? {
        var location: Location? = null

        if (!isPermissionEnable) {
            Timber.e(getString(R.string.location_service_permission_denied))
            return location
        }

        if (bestProvider == null) {
            Timber.e(getString(R.string.location_service_provider_null))
            return location
        }

        //        manager.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, listener)
        manager.requestSingleUpdate(bestProvider!!, listener, null)
        location = manager.getLastKnownLocation(bestProvider!!)

        return location
    }

    private fun removeUpdate() {
        manager.removeUpdates(listener)
    }
}

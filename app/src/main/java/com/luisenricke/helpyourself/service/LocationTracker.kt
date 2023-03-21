package com.luisenricke.helpyourself.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import com.luisenricke.androidext.getBestProvider
import com.luisenricke.androidext.checkPermission
import com.luisenricke.helpyourself.R
import timber.log.Timber

// https://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-on-a?lq=1
// https://stackoverflow.com/questions/20210565/android-location-manager-get-gps-location-if-no-gps-then-get-to-network-provid
@Suppress("unused")
class LocationTrack() : Service() {

    companion object {
        private const val MIN_TIME: Long = 1000L * 60L * 1L // milliseconds * seconds * minutes
        private const val MIN_DISTANCE: Float = 10f         // meters
    }

    private val manager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val bestProvider: String?
        get() = manager.getBestProvider()

    private val isPermissionEnable: Boolean
        get() = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    private val listener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    private var location: Location? = null

    val latitude: Double
        get() = location?.latitude ?: 0.0

    val longitude: Double
        get() = location?.longitude ?: 0.0

    init {
        location = getLocation()
        Timber.i("lat: ${location?.latitude}, lon: ${location?.longitude}")
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
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
        Timber.i("lat: ${location?.latitude}, lon: ${location?.longitude}")

        return location
    }

    override fun onBind(intent: Intent?): IBinder? =
            null
}

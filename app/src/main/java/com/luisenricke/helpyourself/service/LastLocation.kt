package com.luisenricke.helpyourself.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.luisenricke.androidext.getBestProvider
import com.luisenricke.androidext.checkPermission
import com.luisenricke.androidext.isGpsEnable
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.SingletonHolder
import timber.log.Timber

// https://nominatim.openstreetmap.org/reverse?format=xml&lat=17.0734041&lon=-96.7215872&zoom=18&addressdetails=1
// https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=17.0734041&lon=-96.7215872
@Suppress("unused")
class LastLocation private constructor(private val context: Context) {

    companion object : SingletonHolder<LastLocation, Context>(::LastLocation) {
        private const val MIN_TIME: Long = 1000L * 60L * 1L // milliseconds * seconds * minutes
        private const val MIN_DISTANCE: Float = 10f // meters
    }

    //    private lateinit var context: Context
    private val manager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val bestProvider: String?
        get() = manager.getBestProvider()

    private val isPermissionEnable: Boolean
        get() = context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    val isGpsEnable: Boolean
        get() = manager.isGpsEnable()

    private val listener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var location: Location? = null

        if (!isPermissionEnable) {
            Timber.e(context.getString(R.string.location_service_permission_denied))
            return location
        }

        if (bestProvider == null) {
            Timber.e(context.getString(R.string.location_service_provider_null))
            return location
        }

        //        manager.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, listener)
        manager.requestSingleUpdate(bestProvider!!, listener, null)
        location = manager.getLastKnownLocation(bestProvider!!)

        return location
    }

    fun removeUpdate() {
        manager.removeUpdates(listener)
    }
}

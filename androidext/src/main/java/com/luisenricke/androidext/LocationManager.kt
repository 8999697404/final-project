package com.luisenricke.androidext

import android.content.Context
import android.location.LocationManager

private const val TAG = "LocationManagerExt"

@Suppress("unused")
fun LocationManager.getBestProvider(): String? {
    val provider: String?

    val isGPSAvailable = this.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val isNetworkAvailable = this.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    val isPassiveNetworkAvailable = this.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)

    provider = when {
        isGPSAvailable            -> LocationManager.GPS_PROVIDER
        isNetworkAvailable        -> LocationManager.NETWORK_PROVIDER
        isPassiveNetworkAvailable -> LocationManager.PASSIVE_PROVIDER
        else                      -> null
    }

    return provider
}

@Suppress("unused")
fun LocationManager.checkProviders(context: Context) {
    val isGPSEnable = this.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val isNetworkEnable = this.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    val isPassiveEnable = this.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)

    /*
    Log.d(
            TAG, """/* ${System.lineSeparator()}
            |gps:        $isGPSEnable,
            |network:    $isNetworkEnable,
            |passive:    $isPassiveEnable
            ${System.lineSeparator()} */""".trimMargin()
    )

    if (!isGPSEnable) Log.e(TAG, context.getString(R.string.gps_provider_disable))
    if (!isNetworkEnable) Log.e(TAG, context.getString(R.string.network_provider_disable))
    if (!isPassiveEnable) Log.e(TAG, context.getString(R.string.passive_provider_disable))
     */
}

@Suppress("unused")
fun LocationManager.isGpsEnable(): Boolean =
        this.isProviderEnabled(LocationManager.GPS_PROVIDER)

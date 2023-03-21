package com.luisenricke.helpyourself.service

import android.content.Context
import android.net.ConnectivityManager
import com.luisenricke.helpyourself.SingletonHolder
import timber.log.Timber
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException

// https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
@Suppress("unused")
class Connectivity private constructor(private val context: Context) {

    companion object : SingletonHolder<Connectivity, Context>(::Connectivity) {
        const val HOST = "duckduckgo.com"

        const val OPEN_DNS = "208.67.222.222"
        const val GOOGLE_DNS = "8.8.8.8"
    }

    private val manager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var isConnected = false

    fun isInternetAvailable(): Boolean {
        isConnected = false
        try {
            val inetAddresse = InetAddress.getByName(HOST)
            isConnected = !inetAddresse.equals("")
        } catch (e: UnknownHostException) {
            Timber.e("Unknown Host")
        }
        return isConnected
    }

    fun isNetworkAvailableSocket(): Boolean {
        isConnected = false
        var socket: Socket? = null

        try {
            val timeout = 1500
            socket = Socket()
            val address = InetSocketAddress(OPEN_DNS, 53)
            socket.connect(address, timeout)
            isConnected = socket.isConnected
        } catch (e: IOException) {
            Timber.e("Socket not connected")
        } finally {
            socket?.close()
        }

        return isConnected
    }
}

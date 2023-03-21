package com.luisenricke.helpyourself.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.luisenricke.helpyourself.service.SensorForeground
import timber.log.Timber

class AutoStartUpBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("onReceive")

        val service = SensorForeground::class.java

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, service))
            } else {
                context.startService(Intent(context, service))
            }
        }
    }
}


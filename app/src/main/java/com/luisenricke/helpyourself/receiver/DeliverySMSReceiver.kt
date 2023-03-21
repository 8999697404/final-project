package com.luisenricke.helpyourself.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class DeliverySMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Timber.i("DeliverySMSReceiver -> RESULT_OK")
            }
            Activity.RESULT_CANCELED -> {
                Timber.e("DeliverySMSReceiver -> RESULT_CANCELED")
            }
            else                     -> {
                Timber.e("DeliverySMSReceiver -> WTF")
            }
        }
    }
}

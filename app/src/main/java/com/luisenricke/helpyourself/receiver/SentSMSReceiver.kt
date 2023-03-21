package com.luisenricke.helpyourself.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast
import timber.log.Timber

class SentSMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "SentSMS trigged", Toast.LENGTH_SHORT).show()
        when (resultCode) {
            Activity.RESULT_OK                      -> {
                Timber.i("SentSMSReceiver -> RESULT_OK")
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                Timber.e("SentSMSReceiver -> RESULT_ERROR_GENERIC_FAILURE")
            }
            SmsManager.RESULT_ERROR_NO_SERVICE      -> {
                Timber.e("SentSMSReceiver -> RESULT_ERROR_NO_SERVICE")
            }
            SmsManager.RESULT_ERROR_NULL_PDU        -> {
                Timber.e("SentSMSReceiver -> RESULT_ERROR_NULL_PDU")
            }
            SmsManager.RESULT_ERROR_RADIO_OFF       -> {
                Timber.e("SentSMSReceiver -> RESULT_ERROR_RADIO_OFF")
            }
            else                                    -> {
                Timber.e("SentSMSReceiver -> WTF")
            }
        }
    }
}

package com.luisenricke.helpyourself.service

import android.content.Context
import android.location.Location
import android.telephony.SmsManager
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.SingletonHolder
import com.luisenricke.helpyourself.receiver.DeliverySMSReceiver
import com.luisenricke.helpyourself.receiver.SentSMSReceiver
import com.luisenricke.kotlinext.roundDecimals

// https://stackoverflow.com/questions/26893796/how-to-set-emoji-by-unicode-in-a-textview
// https://stackoverflow.com/questions/4967448/show-compose-sms-view-in-android
// https://stackoverflow.com/questions/18771356/check-if-an-sms-is-actually-sent
// https://www.quora.com/Why-does-using-emoji-reduce-my-SMS-character-limit-to-70
// TODO: Fix this with broadcast receiver [SentSMSReceiver, DeliverySMSReceiver]
// TODO: check if it is possible to send messages
@Suppress("unused")
class SendSMS private constructor(private val context: Context) {

    companion object : SingletonHolder<SendSMS, Context>(::SendSMS) {
        @JvmStatic
        var pin = String(Character.toChars(0x1F4CD))
        const val LIMIT_DIGITS = 10

        const val INTENT_SENT = "SMS_SENT"
        const val INTENT_DELIVERY = "SMS_DELIVERED"
    }

    private val manager: SmsManager = SmsManager.getDefault()

    private lateinit var sentBroadcastReceiver: SentSMSReceiver
    private lateinit var deliveryBroadcastReceiver: DeliverySMSReceiver

    fun simpleMessage(phone: String, message: String) {
        manager.sendTextMessage(phone, null, message, null, null)
    }

    fun locationMessage(phone: String, location: Location?) {
        if (location == null) return

        val latitude = location.latitude
        val longitude = location.longitude

        val maps = context.resources.getStringArray(R.array.settings_category_alert_map_list)
        val mapSelected = context.preferenceGet(Constraint.ALERT_MAPS, String::class) ?: maps[0]

        val message = when (mapSelected) {
            maps[0] -> "$pin: ${setOpenStreetMap(latitude, longitude)}"
            maps[1] -> "$pin: ${setGoogleMaps(latitude, longitude)}"
            else    -> context.getString(R.string.sms_service_location_null)
        }

        // Timber.i("SMS message: $message")
        manager.sendTextMessage(phone, null, message, null, null)
    }

    fun getMaxLength(): Int =
            100

    fun unregisterReceivers() {
        context.unregisterReceiver(sentBroadcastReceiver)
        context.unregisterReceiver(deliveryBroadcastReceiver)
    }

    private fun setGoogleMaps(latitude: Double, longitude: Double): String {
        val latitudeFormat: Double = latitude.roundDecimals(LIMIT_DIGITS).toDouble()
        val longitudeFormat: Double = longitude.roundDecimals(LIMIT_DIGITS).toDouble()
        return "http://maps.google.com/?q=$latitudeFormat,$longitudeFormat"
    }

    private fun setOpenStreetMap(latitude: Double, longitude: Double): String {
        val latitudeFormat: Double = latitude.roundDecimals(LIMIT_DIGITS).toDouble()
        val longitudeFormat: Double = longitude.roundDecimals(LIMIT_DIGITS).toDouble()
        return "http://m.osmtools.de/?mlat=${latitudeFormat}&mlon=${longitudeFormat}&icon=4&zoom=19&iframe=1"
    }
}

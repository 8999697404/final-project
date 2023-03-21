package com.luisenricke.helpyourself.alert

import android.content.Context
import android.location.Location
import android.os.Looper
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.AppDatabase
import com.luisenricke.helpyourself.database.dao.AlertContactDAO
import com.luisenricke.helpyourself.database.dao.AlertDAO
import com.luisenricke.helpyourself.database.dao.ContactDAO
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.helpyourself.database.entity.AlertContact
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.helpyourself.service.LastLocation
import com.luisenricke.helpyourself.service.SendSMS
import com.luisenricke.helpyourself.service.Vibration
import timber.log.Timber

@Suppress("unused")
class AlertFacade {

    companion object {
        val TAG = AlertFacade::class.simpleName
    }

    private lateinit var contactDao: ContactDAO
    private lateinit var alertDAO: AlertDAO
    private lateinit var alertContactDAO: AlertContactDAO

    private var isLocationMessageEnable: Boolean = false
    private var isVibrationEnable: Boolean = true

    fun alertTriggeredBackgroundThreat(context: Context) { // TODO Join with coroutine of called
        Looper.prepare()
        val looper = Looper.myLooper()

        initDatabase(context)
        val contacts = contactDao.getHighlighted()

        if (contacts.isEmpty()) {
            Timber.e("Empty contacts")
            looper?.quit()
            return
        }

        vibrate(context)
        buildMessages(context, contacts, LastLocation.getInstance(context).getLocation())

        looper?.quit()
    }

    fun alertTriggeredMainThreat(context: Context) {
        initDatabase(context)
        val contacts = contactDao.getHighlighted()

        if (contacts.isEmpty()) {
            Timber.e("Empty contacts")
            return
        }

        vibrate(context)
        buildMessages(context, contacts, LastLocation.getInstance(context).getLocation())
    }

    private fun initDatabase(context: Context) {
        val database = AppDatabase.getInstance(context)
        contactDao = database.contactDAO()
        alertDAO = database.alertDAO()
        alertContactDAO = database.alertContactDAO()
    }

    private fun vibrate(context: Context) {
        isVibrationEnable = context.preferenceGet(Constraint.ALERT_VIBRATION, Boolean::class) ?: true
        if (isVibrationEnable) {
            Vibration.getInstance(context).vibrate()
        }
    }

    private fun buildMessages(context: Context, contacts: List<Contact>, location: Location?) {
        // configurations
        isLocationMessageEnable = context.preferenceGet(Constraint.ALERT_LOCATION, Boolean::class) ?: false
        val defaultMessage = context.preferenceGet(Constraint.ALERT_DEFAULT_MESSAGE, String::class)
                             ?: context.getString(R.string.alert_message_default)

        // Save Alert
        val alert = Alert(latitude = location?.latitude ?: 0.0, longitude = location?.longitude ?: 0.0)
        val idAlert = alertDAO.insert(alert)

        // Save AlertContacts
        val alertContacts = arrayListOf<AlertContact>()

        for (contact in contacts) {
            val alertContact = AlertContact(alertId = idAlert, contactId = contact.id)

            alertContact.messageSent = if (contact.message.isEmpty()) {
                defaultMessage
            } else {
                contact.message
            }

            alertContacts.add(alertContact)
        }

        alertContactDAO.inserts(alertContacts)

        // Send
        for (alertContact in alertContacts) {
            val contact = contactDao.get(alertContact.contactId)
            SendSMS.getInstance(context).simpleMessage(contact!!.phone, alertContact.messageSent)

            if (isLocationMessageEnable) {
                SendSMS.getInstance(context).locationMessage(contact.phone, location)
            }
        }
    }

}

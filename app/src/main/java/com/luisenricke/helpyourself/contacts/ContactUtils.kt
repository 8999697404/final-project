package com.luisenricke.helpyourself.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.Editable
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.luisenricke.androidext.checkPermission
import com.luisenricke.androidext.intentSelectContact
import com.luisenricke.androidext.requestPermission
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.dao.ContactDAO
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.kotlinext.removeWhiteSpaces

@Suppress("unused")
object ContactUtils {

    fun requestContact(fragment: Fragment) {
        if (fragment.checkPermission(Manifest.permission.READ_CONTACTS)) {
            fragment.intentSelectContact(Constraint.INTENT_READ_CONTACTS_CODE)
        } else {
            fragment.requestPermission(
                    Manifest.permission.READ_CONTACTS,
                    Constraint.PERMISSION_READ_CONTACTS_CODE,
                    fragment.getString(R.string.permission_message_read_contacts_denied),
                    true
            )
        }
    }

    fun getContact(context: Context, data: Intent?): Contact {
        val contact = Contact()

        if (data!!.data == null) return contact

        val contentResolver = context.contentResolver
        val uri = data.data

        val contacts = contentResolver.query(uri!!, null, null, null, null)
        if (contacts!!.moveToFirst()) {
            val id: String = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID)) ?: return contact

            contact.name = contacts.getStringOrNull(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).let { str ->
                if (!str.isNullOrEmpty() && !str.contains("\\d+".toRegex())) {
                    str
                } else {
                    context.getString(R.string.safety_contact)
                }
            }

            val phones = contentResolver.query(Phone.CONTENT_URI, null, "${Phone.CONTACT_ID} = $id", null, null)

            while (phones!!.moveToNext()) {
                val type = phones.getInt(phones.getColumnIndex(Phone.TYPE))
                contact.phone = phones.getStringOrNull(phones.getColumnIndex(Phone.NUMBER)).takeIf {
                    type == Phone.TYPE_MOBILE || type == Phone.TYPE_WORK_MOBILE || type == Phone.TYPE_MAIN
                }.let { str -> str ?: "" }
            }

            phones.close()
        }

        contacts.close()

        return contact
    }

    private fun isValidPhone(phone: String): Boolean {
        val trimPhone = phone.removeWhiteSpaces()
        return if (trimPhone.length < 10 || trimPhone.length > 14) false
        else android.util.Patterns.PHONE.matcher(phone).matches()
    }

    fun hasEmptyField(context: Context, layout: TextInputLayout, editable: Editable): Boolean {
        val isNotEmpty = editable.toString().isNotEmpty()
        val name = layout.hint

        if (isNotEmpty) {
            layout.isErrorEnabled = false
            layout.error = ""
            return false
        }

        layout.isErrorEnabled = true
        layout.error = when (name) {
            context.getString(R.string.contact_name)         -> context.getString(R.string.contact_name_empty_error)
            context.getString(R.string.contact_phone)        -> context.getString(R.string.contact_phone_empty_error)
            context.getString(R.string.contact_relationship) -> context.getString(R.string.contact_relationship_empty_error)
            else                                             -> context.getString(R.string.contact_empty_error_default)
        }
        return true
    }

    fun hasValidPhoneField(context: Context, layout: TextInputLayout, editable: Editable): Boolean {
        val isNotEmpty = editable.toString().isNotEmpty()

        if (isValidPhone(editable.toString()) && isNotEmpty) {
            layout.isErrorEnabled = false
            layout.error = ""
            return false
        }

        if (!isValidPhone(editable.toString())) {
            layout.isErrorEnabled = true
            layout.error = context.getString(R.string.contact_phone_invalid_error)
            return true
        }

        return false
    }

    fun hasPhoneAlreadyExist(context: Context, dao: ContactDAO, exceptPhone: String?, layout: TextInputLayout, editable: Editable): Boolean {
        val phone = editable.toString().removeWhiteSpaces()
        val isNotEmpty = phone.isNotEmpty()
        val countContactsByPhone = dao.countByPhone(phone)

        if ((countContactsByPhone == 0L || (exceptPhone == phone)) && isNotEmpty) {
            layout.isErrorEnabled = false
            layout.error = ""
            return false
        }

        if (countContactsByPhone != 0L) {
            layout.isErrorEnabled = true
            layout.error = context.getString(R.string.contact_phone_already_exist_error)
            return true
        }

        return false
    }

    fun hasMessageExceeded(context: Context, maxLength: Int, layout: TextInputLayout, editable: Editable): Boolean {
        val message = editable.toString()

        if (message.length > maxLength) {
            layout.isErrorEnabled = true
            return true
        }else {
            layout.isErrorEnabled = false
            return false
        }
    }
}

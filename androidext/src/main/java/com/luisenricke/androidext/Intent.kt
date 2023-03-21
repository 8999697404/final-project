package com.luisenricke.androidext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

// region AppCompatActivity
@Suppress("unused")
fun AppCompatActivity.intentSelectContact(requestCode: Int) {
    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    this.startActivityForResult(intent, requestCode)
}

@Suppress("unused")
fun AppCompatActivity.intentSelectImageFromGallery(requestCode: Int) {
    val actionGetContent = Intent(Intent.ACTION_GET_CONTENT)
    actionGetContent.type = "image/*"

    val actionPick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    actionPick.type = "image/*"

    val intent = Intent.createChooser(
        actionGetContent,
        this.getString(R.string.intent_select_image_from_gallery)
    )
    intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(actionPick))

    this.startActivityForResult(intent, requestCode)
}
// endregion

// region Fragment
@Suppress("unused")
fun Fragment.intentSelectContact(requestCode: Int) {
    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    this.startActivityForResult(intent, requestCode)
}

@Suppress("unused")
fun Fragment.intentSelectImageFromGallery(requestCode: Int) {
    val actionGetContent = Intent(Intent.ACTION_GET_CONTENT)
    actionGetContent.type = "image/*"

    val actionPick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    actionPick.type = "image/*"

    val intent = Intent.createChooser(
        actionGetContent,
        this.getString(R.string.intent_select_image_from_gallery)
    )
    intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(actionPick))

    this.startActivityForResult(intent, requestCode)
}
// endregion

// region Context
@Suppress("unused")
fun Context.intentSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    this.startActivity(intent)
}

@Suppress("unused")
fun Context.intentEnableLocation() {
    this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}

@Suppress("unused")
fun Context.intentBrowser(url: String) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    this.startActivity(intent)
}
// endregion

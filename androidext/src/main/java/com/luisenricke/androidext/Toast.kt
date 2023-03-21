package com.luisenricke.androidext

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@Suppress("unused")
fun AppCompatActivity.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun AppCompatActivity.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun Fragment.toastShort(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun Fragment.toastLong(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

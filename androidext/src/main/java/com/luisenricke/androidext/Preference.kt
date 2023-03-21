package com.luisenricke.androidext

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlin.reflect.KClass

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun AppCompatActivity.preferenceSet(variable: String, value: Any) {
    val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
    when (value.getClass()) {
        String::class -> editor.putString(variable, value.toString()).apply()
        Int::class -> editor.putInt(variable, value.toString().toInt()).apply()
        Long::class -> editor.putLong(variable, value.toString().toLong()).apply()
        Float::class -> editor.putFloat(variable, value.toString().toFloat()).apply()
        Boolean::class -> editor.putBoolean(variable, value.toString().toBoolean()).apply()
        else -> throw IllegalArgumentException("The type ${value.getClass().simpleName} isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun <T : Any> AppCompatActivity.preferenceGet(variable: String, type: KClass<T>): T? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    if (!sharedPreferences.all.containsKey(variable)) return null
    return when (type) {
        String::class -> sharedPreferences.getString(variable, "") as T
        Int::class -> sharedPreferences.getInt(variable, -1) as T
        Long::class -> sharedPreferences.getLong(variable, -1L) as T
        Float::class -> sharedPreferences.getFloat(variable, -1F) as T
        Boolean::class -> sharedPreferences.getBoolean(variable, false) as T
        else -> throw IllegalArgumentException("The type $type isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
fun AppCompatActivity.preferenceDelete(variable: String): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    if (!sharedPreferences.all.containsKey(variable)) return false
    return sharedPreferences.edit().remove(variable).commit()
}

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun Fragment.preferenceSet(variable: String, value: Any) {
    val editor = PreferenceManager.getDefaultSharedPreferences(this.context).edit()
    when (value.getClass()) {
        String::class -> editor.putString(variable, value.toString()).apply()
        Int::class -> editor.putInt(variable, value.toString().toInt()).apply()
        Long::class -> editor.putLong(variable, value.toString().toLong()).apply()
        Float::class -> editor.putFloat(variable, value.toString().toFloat()).apply()
        Boolean::class -> editor.putBoolean(variable, value.toString().toBoolean()).apply()
        else -> throw IllegalArgumentException("The type ${value.getClass().simpleName} isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun <T : Any> Fragment.preferenceGet(variable: String, type: KClass<T>): T? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
    if (!sharedPreferences.all.containsKey(variable)) return null
    return when (type) {
        String::class -> sharedPreferences.getString(variable, "") as T
        Int::class -> sharedPreferences.getInt(variable, -1) as T
        Long::class -> sharedPreferences.getLong(variable, -1L) as T
        Float::class -> sharedPreferences.getFloat(variable, -1F) as T
        Boolean::class -> sharedPreferences.getBoolean(variable, false) as T
        else -> throw IllegalArgumentException("The type $type isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
fun Fragment.preferenceDelete(variable: String): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
    if (!sharedPreferences.all.containsKey(variable)) return false
    return sharedPreferences.edit().remove(variable).commit()
}

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun Context.preferenceSet(variable: String, value: Any) {
    val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
    when (value.getClass()) {
        String::class -> editor.putString(variable, value.toString()).apply()
        Int::class -> editor.putInt(variable, value.toString().toInt()).apply()
        Long::class -> editor.putLong(variable, value.toString().toLong()).apply()
        Float::class -> editor.putFloat(variable, value.toString().toFloat()).apply()
        Boolean::class -> editor.putBoolean(variable, value.toString().toBoolean()).apply()
        else -> throw IllegalArgumentException("The type ${value.getClass().simpleName} isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun <T : Any> Context.preferenceGet(variable: String, type: KClass<T>): T? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    if (!sharedPreferences.all.containsKey(variable)) return null
    return when (type) {
        String::class -> sharedPreferences.getString(variable, "") as T
        Int::class -> sharedPreferences.getInt(variable, -1) as T
        Long::class -> sharedPreferences.getLong(variable, -1L) as T
        Float::class -> sharedPreferences.getFloat(variable, -1F) as T
        Boolean::class -> sharedPreferences.getBoolean(variable, false) as T
        else -> throw IllegalArgumentException("The type $type isn't support it to SharedPreferences")
    }
}

@Suppress("unused", "UNCHECKED_CAST")
fun Context.preferenceDelete(variable: String): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    if (!sharedPreferences.all.containsKey(variable)) return false
    return sharedPreferences.edit().remove(variable).commit()
}

private fun <T : Any> T.getClass(): KClass<T> = javaClass.kotlin

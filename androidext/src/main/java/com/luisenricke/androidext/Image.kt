package com.luisenricke.androidext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

// https://stackoverflow.com/a/15718526

@Suppress("unused")
fun Bitmap.toByteArray(): ByteArray? {
    val blob = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)
    return blob.toByteArray()
}

@Suppress("unused")
fun ByteArray.toBitmap(): Bitmap? {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

@Suppress("unused")
fun Context.saveImageInternalStorage(image: Bitmap?, name: String): Boolean {
    if (image == null) return false

    var fos: FileOutputStream? = null
    try {
        fos = this.openFileOutput(name, Context.MODE_PRIVATE)
        image.compress(Bitmap.CompressFormat.PNG, 0, fos)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return false
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    } finally {
        fos?.close()
    }
    return true
}

@Suppress("unused")
fun Context.loadImageInternalStorage(name: String): Bitmap? {
    var image: Bitmap? = null
    var fis: FileInputStream? = null
    try {
        fis = this.openFileInput(name)
        image = BitmapFactory.decodeStream(fis)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        fis?.close()
    }
    return image
}

@Suppress("unused")
fun Context.deleteImageInternalStorage(name: String): Boolean {
    return this.deleteFile(name)
}

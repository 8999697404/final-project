package com.luisenricke.androidext

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment

private const val TAG = "PermissionExt"
private const val EXCEPTION_CONTEXT_NULL = "The context is null"

// region AppCompatActivity
@Suppress("unused")
fun AppCompatActivity.requestPermission(permission: String, requestCode: Int, deniedMessage: String, displayMessage: Boolean = false) {
    val shouldAskPermission = shouldAskPermission(this, permission, requestCode)

    when {
        checkPermission(permission) -> Unit // Log.v(TAG, "${getString(R.string.permission_granted)} $requestCode")
        shouldAskPermission         -> requestPermissions(this, arrayOf(permission), requestCode)
        else                        -> if (displayMessage) toastLong(deniedMessage)
    }
}

@Suppress("unused")
fun AppCompatActivity.requestCriticalPermission(permission: String, requestCode: Int, permissionMessage: PermissionMessage) {
    val shouldAskPermission = shouldAskPermission(this, permission, requestCode)

    when {
        checkPermission(permission) -> Unit //Log.v(TAG, "${getString(R.string.permission_granted)} $requestCode")
        shouldAskPermission         -> requestPermissions(this, arrayOf(permission), requestCode)
        else                        -> alertDialog(this, permissionMessage)
    }
}

@Suppress("unused")
fun AppCompatActivity.requestPermissions(permissions: MutableList<String>, requestCode: Int, deniedMessage: String, displayMessage: Boolean = false) {
    val shouldAskPermissions = shouldAskPermissions(this, permissions, requestCode)

    when {
        checkPermissions(permissions) -> Unit //Log.v(TAG, "${getString(R.string.permissions_granted)} $requestCode")
        shouldAskPermissions          -> requestPermissions(this, permissions.toTypedArray(), requestCode)
        else                          -> if (displayMessage) toastLong(deniedMessage)
    }
}

@Suppress("unused")
fun AppCompatActivity.requestCriticalPermissions(permissions: MutableList<String>, requestCode: Int, permissionMessage: PermissionMessage) {
    val shouldAskPermissions = shouldAskPermissions(this, permissions, requestCode)

    when {
        checkPermissions(permissions) -> Unit //Log.v(TAG, "${getString(R.string.permissions_granted)} $requestCode")
        shouldAskPermissions          -> requestPermissions(this, permissions.toTypedArray(), requestCode)
        else                          -> alertDialog(this, permissionMessage)
    }
}

@Suppress("unused")
fun AppCompatActivity.checkPermission(permission: String): Boolean =
        checkSelfPermission(this, permission) == PERMISSION_GRANTED

@Suppress("unused")
fun AppCompatActivity.checkPermissions(permissions: MutableList<String>): Boolean =
        permissions.all { checkSelfPermission(this, it) == PERMISSION_GRANTED }
// endregion

// region fragment
@Suppress("unused")
fun Fragment.requestPermission(permission: String, requestCode: Int, deniedMessage: String, displayMessage: Boolean = false) {
    val activity = this.requireActivity()
    val shouldAskPermission = shouldAskPermission(activity, permission, requestCode)

    when {
        checkPermission(permission) -> Unit //Log.v(TAG, "${getString(R.string.permission_granted)} $requestCode")
        shouldAskPermission         -> requestPermissions(arrayOf(permission), requestCode)
        else                        -> if (displayMessage) toastLong(deniedMessage)
    }
}

@Suppress("unused")
fun Fragment.requestCriticalPermission(permission: String, requestCode: Int, permissionMessage: PermissionMessage) {
    val activity = this.requireActivity()
    val shouldAskPermission = shouldAskPermission(activity, permission, requestCode)

    when {
        checkPermission(permission) -> Unit //Log.v(TAG, "${getString(R.string.permission_granted)} $requestCode")
        shouldAskPermission         -> requestPermissions(arrayOf(permission), requestCode)
        else                        -> alertDialog(this.requireContext(), permissionMessage)
    }
}

@Suppress("unused")
fun Fragment.requestPermissions(permissions: MutableList<String>, requestCode: Int, deniedMessage: String, displayMessage: Boolean = false) {
    val activity = this.requireActivity()
    val shouldAskPermissions = shouldAskPermissions(activity, permissions, requestCode)

    when {
        checkPermissions(permissions) -> Unit //Log.v(TAG, "${getString(R.string.permissions_granted)} $requestCode")
        shouldAskPermissions          -> requestPermissions(permissions.toTypedArray(), requestCode)
        else                          -> if (displayMessage) toastLong(deniedMessage)
    }
}

@Suppress("unused")
fun Fragment.requestCriticalPermissions(permissions: MutableList<String>, requestCode: Int, permissionMessage: PermissionMessage) {
    val activity = this.requireActivity()
    val shouldAskPermissions = shouldAskPermissions(activity, permissions, requestCode)

    when {
        checkPermissions(permissions) -> Unit //Log.v(TAG, "${getString(R.string.permissions_granted)} $requestCode")
        shouldAskPermissions          -> requestPermissions(permissions.toTypedArray(), requestCode)
        else                          -> alertDialog(this.requireContext(), permissionMessage)
    }
}

@Suppress("unused")
fun Fragment.checkPermission(permission: String): Boolean =
        checkSelfPermission(this.requireContext(), permission) == PERMISSION_GRANTED

@Suppress("unused")
fun Fragment.checkPermissions(permissions: MutableList<String>): Boolean =
        permissions.all { checkSelfPermission(this.requireContext(), it) == PERMISSION_GRANTED }
// endregion

// region context
@Suppress("unused")
@Throws(NullPointerException::class)
fun Context?.checkPermission(permission: String): Boolean {
    if (this == null) throw NullPointerException(EXCEPTION_CONTEXT_NULL)
    return checkSelfPermission(this, permission) == PERMISSION_GRANTED
}

@Suppress("unused")
@Throws(NullPointerException::class)
fun Context?.checkPermissions(permissions: MutableList<String>): Boolean {
    if (this == null) throw NullPointerException(EXCEPTION_CONTEXT_NULL)
    return permissions.all { checkSelfPermission(this, it) == PERMISSION_GRANTED }
}
// endregion

// region PermissionMessage
data class PermissionMessage(
    val context: Context,
    var title: String = context.getString(R.string.permissions_title),
    var message: String = context.getString(
        R.string.permission_message
),
    var positiveButton: String = context.getString(R.string.permission_positive_button),
    var negativeButton: String = context.getString(R.string.permission_negative_button),
    var denied: String = context.getString(
        R.string.permission_denied
)
)

private fun alertDialog(context: Context, permissionMessage: PermissionMessage) =
        AlertDialog.Builder(context)
                .setTitle(permissionMessage.title)
                .setMessage(permissionMessage.message)
                .setPositiveButton(permissionMessage.positiveButton) { _: DialogInterface, _: Int ->
                    context.intentSettings()
                }
                .setNegativeButton(permissionMessage.negativeButton) { dialog: DialogInterface, _: Int ->
                    context.toastShort(permissionMessage.denied)
                    dialog.dismiss()
                }
                .show()
// endregion PermissionMessage

// region HelpFunctions
private fun shouldAskPermission(activity: Activity, permission: String, requestCode: Int): Boolean {
    val preference = "${requestCode}_first_time_to_ask_permission"
    val isFirstAsked = activity.preferenceGet(preference, Boolean::class) ?: true
    if (isFirstAsked) activity.preferenceSet(preference, false)
    return shouldShowRequestPermissionRationale(activity, permission) xor isFirstAsked
}

private fun shouldAskPermissions(activity: Activity, permissions: MutableList<String>, requestCode: Int): Boolean {
    val preference = "${requestCode}_first_time_to_ask_permissions"
    val isFirstAsked = activity.preferenceGet(preference, Boolean::class) ?: true
    if (isFirstAsked) activity.preferenceSet(preference, false)

    var shouldAsk = false
    for (permission in permissions) {
        if (shouldShowRequestPermissionRationale(activity, permission) xor isFirstAsked) shouldAsk = true
    }

    return shouldAsk
}
// endregion HelpFunctions

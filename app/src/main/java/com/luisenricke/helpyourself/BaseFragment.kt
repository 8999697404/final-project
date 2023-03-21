package com.luisenricke.helpyourself

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luisenricke.androidext.deleteImageInternalStorage
import com.luisenricke.androidext.intentSelectContact
import com.luisenricke.androidext.intentSelectImageFromGallery
import com.luisenricke.androidext.toastLong
import com.luisenricke.helpyourself.database.AppDatabase
import com.luisenricke.helpyourself.service.LastLocation
import com.luisenricke.helpyourself.service.SensorForeground
import timber.log.Timber

@Suppress("unused")
abstract class BaseFragment : Fragment() {

    val navController: NavController
        get() = findNavController()

    val database: AppDatabase
        get() = AppDatabase.getInstance(getActivityContext())

    fun getActivityContext(): MainActivity =
            (activity as MainActivity)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty()) {
            Timber.e(getString(R.string.on_request_permissions_result_grant_results))
            return
        }

        when (requestCode) {
            Constraint.PERMISSION_READ_CONTACTS_CODE -> {
                val isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (isGranted) intentSelectContact(Constraint.INTENT_READ_CONTACTS_CODE)
                else Timber.e("${getString(R.string.on_request_permissions_result_denied_permissions)} READ_CONTACTS")

                return
            }

            Constraint.PERMISSIONS_ALERT_SERVICE -> {
                val isGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

                if (isGranted) {
                    SensorForeground.startService(getActivityContext())

                    if (!LastLocation.getInstance(getActivityContext()).isGpsEnable) {
                        toastLong(getString(R.string.home_gps_request))
                    }
                } else {
                    Timber.e("${getString(R.string.on_request_permissions_result_denied_permissions)} ALERT")
                }

                return
            }

            else                                     -> Timber.e(getString(R.string.on_request_permissions_result_doesnt_find_permission))
        }

    }

    fun setupActionBar(actionBar: ActionBar?, title: String) {
        if (actionBar == null) return

        setHasOptionsMenu(true)
        actionBar.apply {
            this.title = title
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
        }
    }

    // TODO make manager for ImageOptions
    fun imageOptionsSimple(context: Context, view: ImageView) {

        val resource = context.resources
        val options: Array<out String> = if (view.tag == context.getString(R.string.photo_image_tag_default)) resource.getStringArray(R.array.photo_options_empty)
        else resource.getStringArray(R.array.photo_options_filled)

        MaterialAlertDialogBuilder(context).setTitle(resource.getString(R.string.photo_options_title)).setItems(options) { dialog, which ->
            when (options[which]) {
                context.getString(R.string.photo_options_gallery) -> intentSelectImageFromGallery(Constraint.INTENT_IMAGE_FROM_GALLERY)
                context.getString(R.string.photo_options_delete) -> view.apply {
                    setImageResource(R.drawable.ic_baseline_person_24)
                    tag = context.getString(R.string.photo_image_tag_default)
                }
                context.getString(R.string.photo_options_cancel) -> dialog.dismiss()
            }
        }.show()
    }

    fun imageOptionsWipe(context: Context, view: ImageView, file: String) {

        val resource = context.resources
        val options: Array<out String> = if (view.tag == context.getString(R.string.photo_image_tag_default)) resource.getStringArray(R.array.photo_options_empty)
        else resource.getStringArray(R.array.photo_options_filled)

        MaterialAlertDialogBuilder(context).setTitle(resource.getString(R.string.photo_options_title)).setItems(options) { dialog, which ->
            when (options[which]) {
                context.getString(R.string.photo_options_gallery) -> intentSelectImageFromGallery(Constraint.INTENT_IMAGE_FROM_GALLERY)
                context.getString(R.string.photo_options_delete) -> {
                    view.apply {
                        setImageResource(R.drawable.ic_baseline_person_24)
                        tag = context.getString(R.string.photo_image_tag_default)
                    }
                    context.deleteImageInternalStorage(file)
                }
                context.getString(R.string.photo_options_cancel) -> dialog.dismiss()
            }
        }.show()
    }

    // TODO test this
    fun getImage(context: Context, data: Intent?): Bitmap? {
        val contentURI: Uri = data?.data!!
        var bitmap: Bitmap? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, contentURI)
            bitmap = ImageDecoder.decodeBitmap(source)

        } else {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
        }

        return bitmap!!
    }
}

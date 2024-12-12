package com.afrinacapstone.ecocraft.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import java.io.File
import java.util.UUID

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Fragment.navigateWithBundle(
    @IdRes resId: Int, bundle: Bundle, extras: Navigator.Extras? = null
) {
    findNavController().navigate(resId, bundle, null, extras)
}

fun Fragment.navigateById(@IdRes resId: Int) {
    findNavController().navigate(resId)
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Context.saveImageAndGetPath(uriData: Uri, onSuccessSave: (String) -> Unit) {
    try {
        val pathFromImagePicker = this.getPictPathFromUri(uriData)!!
        val ext = pathFromImagePicker.substringAfterLast(".", "")
        val fileName = "${UUID.randomUUID()}.$ext"

        val locationUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(
                MediaStore.Images.Media.MIME_TYPE, when {
                    ext.equals("jpg", ignoreCase = true) || ext.equals(
                        "jpeg",
                        ignoreCase = true
                    ) -> "image/jpeg"

                    ext.equals("png", ignoreCase = true) -> "image/png"
                    ext.equals("webp", ignoreCase = true) -> "image/webp"
                    else -> throw IllegalArgumentException("Tipe file tidak didukung")
                }
            )
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/EcoCraft")
        }

        val uri = contentResolver.insert(locationUri, contentValues)
        val inputStream = contentResolver.openInputStream(uriData) ?: return
        uri?.let { outputUri ->
            contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                inputStream.use { input ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            onSuccessSave(this.getPictPathFromUri(outputUri) ?: "")
        }
        inputStream.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Context.getPictPathFromUri(uri: Uri): String? {
    return try {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, filePathColumn, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                picturePath
            } else {
                null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ImageView.loadFromUrl(url: String) {
    Glide
        .with(this.context)
        .load(url)
        .into(this)
}

fun ImageView.loadFromPath(path: String) {
    if (path.isNotBlank()) {
        Glide
            .with(this.context)
            .load(File(path))
            .into(this)
    }
}
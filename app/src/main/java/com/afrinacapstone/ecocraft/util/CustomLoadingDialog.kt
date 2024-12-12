package com.afrinacapstone.ecocraft.util

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AlertDialog
import com.afrinacapstone.ecocraft.R

fun loadingDialog(context: Context): Dialog {
    val dialog = AlertDialog.Builder(context)
        .setView(R.layout.dialog_custom_loading)
        .setCancelable(false)
        .create()

    val width = Resources.getSystem().displayMetrics.widthPixels

    dialog.window!!.setLayout(width / 2, LayoutParams.WRAP_CONTENT)
    return dialog
}

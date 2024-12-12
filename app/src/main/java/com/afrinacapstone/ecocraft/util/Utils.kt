package com.afrinacapstone.ecocraft.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun hasRequiredPermissions(context: Context, permissions: Array<String>): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
package com.afrinacapstone.ecocraft.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    val dateFormat1 = SimpleDateFormat("MMM dd, yyyy, h:mm:ss a", Locale.ENGLISH)

    fun String.parseDate(): Date {
        val date = dateFormat1.parse(this)
        return date!!
    }

}
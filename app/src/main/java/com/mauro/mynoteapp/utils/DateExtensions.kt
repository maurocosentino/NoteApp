package com.mauro.mynoteapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant.ofEpochMilli
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateStringModern(pattern: String = "dd/MM/yyyy HH:mm:ss"): String {
    val timestampInMillis = if (this.toString().length == 10) this * 1000 else this

    val instant = ofEpochMilli(timestampInMillis)
    val formatter = DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}

val timestamp: Long = System.currentTimeMillis()
@RequiresApi(Build.VERSION_CODES.O)
val formattedDate: String = timestamp.toFormattedDateStringModern()

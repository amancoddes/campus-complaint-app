package com.example.soul
import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(ts: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(ts))
}
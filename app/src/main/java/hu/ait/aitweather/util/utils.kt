package hu.ait.aitweather.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertTimestampToTime(timestamp: Int?): String? {
    if (timestamp == null) return null

    // Create a Date object from the UNIX timestamp
    val date = Date(timestamp.toLong() * 1000)

    // Create a SimpleDateFormat instance with desired format
    val format = SimpleDateFormat("hh:mm a", Locale.US)

    // Format the date object to a string
    return format.format(date)
}

data class Settings(
    val units: String = "metric",
    val appID: String = "b825b64d68c0d07bdec67c55b4a20bb2"
)
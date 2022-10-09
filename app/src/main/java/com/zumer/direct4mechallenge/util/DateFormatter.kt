package com.zumer.direct4mechallenge.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    companion object {
        fun formatDateFromString(stringDate: String?, outputFormat: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault())
            return if (stringDate.isNullOrBlank()) {
                ""
            } else {
                val date =
                    simpleDateFormat.parse(stringDate)
                        ?.let { SimpleDateFormat(outputFormat, Locale.getDefault()).format(it) }
                date ?: ""
            }
        }
    }
}
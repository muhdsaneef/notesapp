package com.example.notes.utils

import com.example.notes.constants.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {
    companion object {
        fun getNotesCreatedFormattedDate(createdTimestamp: Long): String {
            val dateFormatter = SimpleDateFormat(AppConstants.NOTE_CREATED_DATE_FORMAT, Locale.getDefault())
            val createdDate = Date(createdTimestamp)
            return dateFormatter.format(createdDate)
        }
    }
}
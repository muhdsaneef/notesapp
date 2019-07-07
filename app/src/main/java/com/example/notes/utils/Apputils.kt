package com.example.notes.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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

        fun showSoftKeyboard(context: Context, view: View) {
            view.postDelayed({
                if (view.requestFocus()) {
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY)
                }
            }, 100)
        }
    }
}
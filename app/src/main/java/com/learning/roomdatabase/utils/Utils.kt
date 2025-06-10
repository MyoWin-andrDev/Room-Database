package com.learning.roomdatabase.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun TextInputEditText.validateNotEmpty(errorMessage: String): Boolean {
    return if (text.toString().trim().isEmpty()) {
        error = errorMessage
        false
    } else {
        error = null
        true
    }
}
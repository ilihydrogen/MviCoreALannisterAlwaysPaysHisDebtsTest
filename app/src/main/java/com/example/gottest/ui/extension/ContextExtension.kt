package com.example.gottest.ui.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(throwable: Throwable) {
    Toast.makeText(this, throwable.localizedMessage, Toast.LENGTH_LONG).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
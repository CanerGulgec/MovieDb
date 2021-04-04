package com.caner.common.ext

import android.app.Activity
import android.widget.Toast

fun Activity.toast(message: String?) =
    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

package com.arkueid.onair.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(applicationContext, msg, duration).show()
    }
}
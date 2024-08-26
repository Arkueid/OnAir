package com.arkueid.onair.utils

import android.content.Context
import android.text.SpannableString
import android.widget.Toast

object ToastUtils {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    fun showContextToast(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, msg, duration).show()
    }

    fun showContextToast(
        context: Context,
        msg: SpannableString,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, msg, duration).show()
    }

    fun showToast(
        msg: SpannableString,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(applicationContext, msg, duration).show()
    }

    fun showToast(
        msg: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(applicationContext, msg, duration).show()
    }
}
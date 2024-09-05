package com.arkueid.onair.common

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author: Arkueid
 * @date: 2024/9/5
 * @desc:
 */
val Int.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Int.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

val Long.timeString: String
    get() {
        val hour = this / 3600000
        val minute = this % 3600000 / 60000
        val second = this % 60000 / 1000
        return if (hour > 0) {
            "%02d:%02d:%02d".format(hour, minute, second)
        } else {
            "%02d:%02d".format(minute, second)
        }
    }
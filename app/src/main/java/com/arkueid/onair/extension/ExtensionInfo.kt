package com.arkueid.onair.extension

import android.graphics.drawable.Drawable

/**
 * @author: Arkueid
 * @date: 2024/9/5
 * @desc:
 */
data class ExtensionInfo(
    val key: String,
    val name: String,
    val version: String,
    val icon: Drawable?,
)
package com.arkueid.onair.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
@Parcelize
data class SearchHistory(
    val content: String,
    val timestamp: Long
) : Parcelable

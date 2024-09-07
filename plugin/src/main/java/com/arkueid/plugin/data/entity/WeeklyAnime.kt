package com.arkueid.plugin.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 番剧
@Parcelize
data class WeeklyAnime(
    val anime: Anime,
    // 星期
    val weekday: Int,
) : Parcelable
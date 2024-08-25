package com.arkueid.onair.presentation.weekly.model

import java.io.Serializable

// 番剧
data class WeeklySubjectHolder(
    // 标题
    val title: String,
    // 番剧封面
    val cover: String,
    // 星期
    val weekday: Int,
) : Serializable
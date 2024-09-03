package com.arkueid.onair.domain.entity

import java.io.Serializable

// 番剧
data class WeeklySubject(
    val anime: Anime,
    // 星期
    val weekday: Int,
) : Serializable

typealias WeeklyData = List<List<WeeklySubject>>

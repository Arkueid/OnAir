package com.arkueid.onair.domain.entity

import java.io.Serializable

// 番剧
data class WeeklySubject(
    // 标题
    val title: String,
    // 番剧封面
    val cover: String,
    // 星期
    val weekday: Int,
) : Serializable

typealias WeeklyData = List<List<WeeklySubject>>

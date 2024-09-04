package com.arkueid.onair.data.source.bangumi.model.weekly

data class CalendarResponseItem(
    val items: List<Item>,
    val weekday: Weekday
)
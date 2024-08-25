package com.arkueid.onair.data.api.bangumi.model.weekly

data class CalendarResponseItem(
    val items: List<Item>,
    val weekday: Weekday
)
package com.arkueid.onair.data.source.bangumi

import com.arkueid.onair.data.source.bangumi.model.weekly.CalendarResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/calendar")
    suspend fun getWeekly(): CalendarResponse
}
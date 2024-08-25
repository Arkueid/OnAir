package com.arkueid.onair.data.api.bangumi

import com.arkueid.onair.data.api.bangumi.model.weekly.CalendarResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/calendar")
    suspend fun getWeekly(): CalendarResponse
}
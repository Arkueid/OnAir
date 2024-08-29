package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.ModuleData
import com.arkueid.onair.domain.entity.SearchResultData
import com.arkueid.onair.domain.entity.SearchTipData
import com.arkueid.onair.domain.entity.WeeklyData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface Repository {

    fun getWeekly(): Flow<WeeklyData>

    fun getHome(): Flow<ModuleData>

    fun getSearchTip(query: String): Flow<SearchTipData>

    fun getSearchResult(query: String): Flow<SearchResultData>
}
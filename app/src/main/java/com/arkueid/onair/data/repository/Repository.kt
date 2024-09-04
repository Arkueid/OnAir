package com.arkueid.onair.data.repository

import com.arkueid.onair.entity.ModuleData
import com.arkueid.onair.entity.SearchResultData
import com.arkueid.onair.entity.SearchTipData
import com.arkueid.onair.entity.WeeklyData
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWeekly(): Flow<WeeklyData>

    fun getHome(): Flow<ModuleData>

    fun getSearchTip(query: String): Flow<SearchTipData>

    fun getSearchResult(query: String): Flow<SearchResultData>
}
package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.ModuleData
import com.arkueid.onair.domain.SearchResult
import com.arkueid.onair.domain.SearchTipData
import com.arkueid.onair.domain.WeeklyData
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWeekly(): Flow<WeeklyData>

    fun getHome(): Flow<ModuleData>

    fun getSearchTip(query: String): Flow<SearchTipData>

    fun getSearchResult(query: String): Flow<SearchResult>
}
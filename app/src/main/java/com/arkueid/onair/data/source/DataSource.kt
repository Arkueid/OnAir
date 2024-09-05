package com.arkueid.onair.data.source

import com.arkueid.onair.domain.ModuleData
import com.arkueid.onair.domain.SearchResult
import com.arkueid.onair.domain.SearchTipData
import com.arkueid.onair.domain.WeeklyData
import kotlinx.coroutines.flow.Flow


interface DataSource {

    fun getWeeklyData(): Flow<WeeklyData>

    fun getModuleData(): Flow<ModuleData>

    fun getSearchTipData(query: String): Flow<SearchTipData>

    fun getSearchResultData(query: String): Flow<SearchResult>
}
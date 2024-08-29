package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.ModuleData
import com.arkueid.onair.domain.entity.SearchResultData
import com.arkueid.onair.domain.entity.SearchTipData
import com.arkueid.onair.domain.entity.WeeklyData
import kotlinx.coroutines.flow.Flow


interface DataSource {

    fun getWeeklyData(): Flow<WeeklyData>

    fun getModuleData(): Flow<ModuleData>

    fun getSearchTipData(query: String): Flow<SearchTipData>

    fun getSearchResultData(query: String): Flow<SearchResultData>
}
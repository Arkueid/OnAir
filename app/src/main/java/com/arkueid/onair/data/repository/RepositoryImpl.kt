package com.arkueid.onair.data.repository

import com.arkueid.onair.data.source.DataSource
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.SearchResult
import com.arkueid.onair.domain.SearchTipData
import com.arkueid.onair.domain.WeeklyData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSource: DataSource) :
    Repository {
    override fun getWeekly(): Flow<WeeklyData> {
        return dataSource.getWeeklyData()
    }

    override fun getHome(): Flow<List<Module>> {
        return dataSource.getModuleData()
    }

    override fun getSearchTip(query: String): Flow<SearchTipData> {
        return dataSource.getSearchTipData(query)
    }

    override fun getSearchResult(query: String): Flow<SearchResult> {
        return dataSource.getSearchResultData(query)
    }
}

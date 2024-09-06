package com.arkueid.onair.data.repository

import com.arkueid.onair.data.source.DataSource
import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.Danmaku
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.SearchResult
import com.arkueid.onair.domain.entity.SearchTip
import com.arkueid.onair.domain.entity.WeeklyAnime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSource: DataSource) :
    Repository {
    override fun getWeekly(): Flow<List<List<WeeklyAnime>>> {
        return dataSource.getWeeklyData()
    }

    override fun getHome(): Flow<List<Module>> {
        return dataSource.getModuleData()
    }

    override fun getSearchTip(query: String): Flow<List<SearchTip>> {
        return dataSource.getSearchTipData(query)
    }

    override fun getSearchResult(query: String): Flow<List<SearchResult>> {
        return dataSource.getSearchResultData(query)
    }

    override fun getDanmakus(anime: Anime): Flow<List<Danmaku>> {
        return dataSource.getDanmakuData(anime)
    }
}

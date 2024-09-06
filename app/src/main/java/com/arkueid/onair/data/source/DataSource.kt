package com.arkueid.onair.data.source


import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.Danmaku
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.SearchResult
import com.arkueid.onair.domain.entity.SearchTip
import com.arkueid.onair.domain.entity.WeeklyAnime
import kotlinx.coroutines.flow.Flow


interface DataSource {
    val sourceId: String
    val sourceName: String

    fun getWeeklyData(): Flow<List<List<WeeklyAnime>>>

    fun getModuleData(): Flow<List<Module>>

    fun getSearchTipData(query: String): Flow<List<SearchTip>>

    fun getSearchResultData(query: String): Flow<List<SearchResult>>

    fun getDanmakuData(anime: Anime): Flow<List<Danmaku>>
}
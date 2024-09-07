package com.arkueid.onair.data.repository

import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.plugin.data.entity.WeeklyAnime
import com.arkueid.plugin.data.source.Source
import kotlinx.coroutines.flow.Flow

interface Repository {

    var source: Source?

    val defaultSource: Source

    fun getWeekly(): Flow<List<List<WeeklyAnime>>>

    fun getHome(): Flow<List<Module>>

    fun getSearchTip(query: String): Flow<List<SearchTip>>

    fun getSearchResult(query: String): Flow<List<SearchResult>>

    fun getDanmakus(anime: Anime): Flow<List<Danmaku>>
}
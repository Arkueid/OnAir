package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.Danmaku
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.SearchResult
import com.arkueid.onair.domain.entity.SearchTip
import com.arkueid.onair.domain.entity.WeeklyAnime
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWeekly(): Flow<List<List<WeeklyAnime>>>

    fun getHome(): Flow<List<Module>>

    fun getSearchTip(query: String): Flow<List<SearchTip>>

    fun getSearchResult(query: String): Flow<List<SearchResult>>

    fun getDanmakus(anime: Anime): Flow<List<Danmaku>>
}
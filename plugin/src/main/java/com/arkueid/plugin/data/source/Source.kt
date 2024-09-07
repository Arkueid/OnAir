package com.arkueid.plugin.data.source


import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.plugin.data.entity.WeeklyAnime


interface Source {
    val sourceId: String get() = this.javaClass.name
    val sourceName: String

    fun getWeeklyData(): List<List<WeeklyAnime>>

    fun getModuleData(): List<Module>

    fun getSearchTipData(query: String): List<SearchTip>

    fun getSearchResultData(query: String): List<SearchResult>

    fun getDanmakuData(anime: Anime): List<Danmaku>
}
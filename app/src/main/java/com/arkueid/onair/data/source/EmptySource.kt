package com.arkueid.onair.data.source

import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.plugin.data.entity.WeeklyAnime
import com.arkueid.plugin.data.source.Source

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
class EmptySource : Source {
    override val sourceId: String
        get() = "com.arkueid.source.empty"
    override val sourceName: String
        get() = "无数据源"

    override fun getWeeklyData(): List<List<WeeklyAnime>> {
        return emptyList()
    }

    override fun getModuleData(): List<Module> {
        return emptyList()
    }

    override fun getSearchTipData(query: String): List<SearchTip> {
        return emptyList()
    }

    override fun getSearchResultData(query: String): List<SearchResult> {
        return emptyList()
    }

    override fun getDanmakuData(anime: Anime): List<Danmaku> {
        return emptyList()
    }
}
package com.arkueid.onair.data.repository

import android.util.Log
import com.arkueid.plugin.data.source.Source
import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.plugin.data.entity.WeeklyAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RepositoryImpl(override var source: Source?, override val defaultSource: Source) :
    Repository {

    companion object {
        private const val TAG = "RepositoryImpl"
    }

    override fun getWeekly(): Flow<List<List<WeeklyAnime>>> {
        return flow {
            emit(source?.getWeeklyData() ?: defaultSource.getWeeklyData())
        }.flowOn(Dispatchers.IO)
    }

    override fun getHome(): Flow<List<Module>> {
        return flow {
            emit(source?.getModuleData() ?: defaultSource.getModuleData())
        }.flowOn(Dispatchers.IO)
    }

    override fun getSearchTip(query: String): Flow<List<SearchTip>> {
        return flow {
            emit(source?.getSearchTipData(query) ?: defaultSource.getSearchTipData(query))
        }.flowOn(Dispatchers.IO)
    }

    override fun getSearchResult(query: String): Flow<List<SearchResult>> {
        return flow {
            emit(source?.getSearchResultData(query) ?: defaultSource.getSearchResultData(query))
        }.flowOn(Dispatchers.IO)
    }

    override fun getDanmakus(anime: Anime): Flow<List<Danmaku>> {
        return flow {
            emit(source?.getDanmakuData(anime) ?: defaultSource.getDanmakuData(anime))
        }.flowOn(Dispatchers.IO)
    }
}

package com.arkueid.onair.data.api.bangumi

import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.ModuleData
import com.arkueid.onair.domain.entity.SearchResultData
import com.arkueid.onair.domain.entity.SearchTipData
import com.arkueid.onair.domain.entity.WeeklyData
import com.arkueid.onair.domain.entity.WeeklySubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BangumiSource(private val okHttpClient: OkHttpClient) : DataSource {

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.bgm.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun getWeeklyData(): Flow<WeeklyData> {
        return flow {
            apiService.getWeekly().map { responseItem ->
                responseItem.items.map { subject ->
                    WeeklySubject(
                        Anime(
                            subject.name_cn.ifEmpty { subject.name },
                            subject.images.common,
                            "" // TODO: get bangumi url
                        ),
                        subject.air_weekday
                    )
                }
            }.let {
                emit(it)
            }
        }
    }

    override fun getModuleData(): Flow<ModuleData> {
        TODO("Not yet implemented")
    }

    override fun getSearchTipData(query: String): Flow<SearchTipData> {
        TODO("Not yet implemented")
    }

    override fun getSearchResultData(query: String): Flow<SearchResultData> {
        TODO("Not yet implemented")
    }
}
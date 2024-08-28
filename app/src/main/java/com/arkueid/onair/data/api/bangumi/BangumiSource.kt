package com.arkueid.onair.data.api.bangumi

import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.domain.entity.ModuleDataHolder
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
                        subject.name_cn.ifEmpty { subject.name },
                        subject.images.common,
                        subject.air_weekday
                    )
                }
            }.let {
                emit(it)
            }
        }
    }

    override fun getModuleData(): Flow<ModuleDataHolder> {
        TODO("Not yet implemented")
    }
}
package com.arkueid.onair.data.repository

import com.arkueid.onair.presentation.weekly.model.WeeklyDataHolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeeklyRepositoryImpl @Inject constructor(private val dataSource: DataSource) :
    WeeklyRepository {
    override fun getWeekly(): Flow<WeeklyDataHolder> {
        return dataSource.getWeeklyData()
    }
}

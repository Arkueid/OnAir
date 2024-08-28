package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.WeeklyData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSource: DataSource) :
    Repository {
    override fun getWeekly(): Flow<WeeklyData> {
        return dataSource.getWeeklyData()
    }

    override fun getHome(): Flow<List<Module>> {
        return dataSource.getModuleData()
    }
}

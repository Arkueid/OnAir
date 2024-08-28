package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.ModuleDataHolder
import com.arkueid.onair.domain.entity.WeeklyData
import kotlinx.coroutines.flow.Flow


interface DataSource {

    fun getWeeklyData(): Flow<WeeklyData>

    fun getModuleData(): Flow<ModuleDataHolder>
}
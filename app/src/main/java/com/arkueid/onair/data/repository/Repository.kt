package com.arkueid.onair.data.repository

import com.arkueid.onair.domain.entity.ModuleDataHolder
import com.arkueid.onair.domain.entity.WeeklyData
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWeekly(): Flow<WeeklyData>

    fun getHome(): Flow<ModuleDataHolder>
}
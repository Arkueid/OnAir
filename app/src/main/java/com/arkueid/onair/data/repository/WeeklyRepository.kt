package com.arkueid.onair.data.repository

import com.arkueid.onair.presentation.weekly.model.WeeklyDataHolder
import kotlinx.coroutines.flow.Flow

interface WeeklyRepository {

    fun getWeekly(): Flow<WeeklyDataHolder>
}
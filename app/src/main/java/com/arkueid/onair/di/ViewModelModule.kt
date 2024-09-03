package com.arkueid.onair.di

import com.arkueid.onair.ui.following.FollowingViewModel
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author: Arkueid
 * @date: 2024/9/3
 * @desc:
 */
@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Singleton
    @Provides
    fun provideFollowingViewModel(gson: Gson, mmkv: MMKV): FollowingViewModel {
        return FollowingViewModel(gson, mmkv)
    }

}
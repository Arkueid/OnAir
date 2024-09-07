package com.arkueid.onair.di

import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.data.repository.RepositoryImpl
import com.arkueid.onair.data.source.EmptySource
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
object NetworkModule {

    @Singleton
    @Provides
    fun provideRepository(): Repository {
        return RepositoryImpl(null, EmptySource())
    }
}
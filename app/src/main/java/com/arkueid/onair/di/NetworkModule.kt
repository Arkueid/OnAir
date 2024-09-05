package com.arkueid.onair.di

import com.arkueid.onair.data.TestDataSource
import com.arkueid.onair.data.source.mikan.MikanSource
import com.arkueid.onair.data.source.DataSource
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.ResourceBundle
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author: Arkueid
 * @date: 2024/9/3
 * @desc:
 */

private val requestHeaderInterceptor = Interceptor { chain ->
    val request = chain.request()
    request.newBuilder()
        .addHeader(
            "User-Agent",
            ResourceBundle.getBundle("dev").getString("user-agent")
        )
        .build()
    chain.proceed(request)
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(requestHeaderInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("mikan")
    fun provideMikanDataSource(): DataSource {
        return MikanSource(okHttpClient)
    }

    @Singleton
    @Provides
    @Named("test")
    fun provideTestDataSource(): DataSource {
        return TestDataSource(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideRepository(@Named("test") dataSource: DataSource): Repository {
        return RepositoryImpl(dataSource)
    }
}
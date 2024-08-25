package com.arkueid.onair.di

import com.arkueid.onair.data.api.bangumi.BangumiSource
import com.arkueid.onair.data.api.mikan.MikanSource
import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.data.repository.WeeklyRepository
import com.arkueid.onair.data.repository.WeeklyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.ResourceBundle
import javax.inject.Named
import javax.inject.Singleton

private val interceptor = Interceptor { chain ->
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
object AppModule {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("bangumi")
    fun provideBangumiDataSource(): DataSource {
        return BangumiSource(okHttpClient)
    }

    @Singleton
    @Provides
    @Named("mikan")
    fun provideMikanDataSource(): DataSource {
        return MikanSource(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideWeeklyRepository(@Named("mikan") dataSource: DataSource): WeeklyRepository {
        return WeeklyRepositoryImpl(dataSource)
    }
}
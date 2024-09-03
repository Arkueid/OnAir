package com.arkueid.onair.di

import com.arkueid.onair.data.TestDataSource
import com.arkueid.onair.data.api.bangumi.BangumiSource
import com.arkueid.onair.data.api.mikan.MikanSource
import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.data.repository.RepositoryImpl
import com.arkueid.onair.ui.following.FollowingViewModel
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.ResourceBundle
import javax.inject.Named
import javax.inject.Singleton

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
object AppModule {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(requestHeaderInterceptor)
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
    @Named("test")
    fun provideTestDataSource(): DataSource {
        return TestDataSource(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideRepository(@Named("test") dataSource: DataSource): Repository {
        return RepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideMMKV() = MMKV.defaultMMKV()
}
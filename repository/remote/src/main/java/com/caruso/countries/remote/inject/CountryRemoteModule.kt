package com.caruso.countries.remote.inject

import com.caruso.countries.domain.AppConfig
import com.caruso.countries.domain.CacheDirProvider
import com.caruso.countries.remote.CountryRemoteDataSource
import com.caruso.countries.remote.adapter.CallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountryRemoteModule {
    // 10 MB
    private const val HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10L * 1024L * 1024L
    private val TIMEOUT = TimeUnit.SECONDS.toSeconds(30)

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        cacheDirProvider: CacheDirProvider,
        repositoryConfig: AppConfig
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val cacheBaseDir = cacheDirProvider.cacheDir
        if (cacheBaseDir != null) {
            val cacheDir = File(cacheBaseDir, "HttpResponseCache")
            builder.cache(Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))
        }
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

        if (repositoryConfig.debugEnabled) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            prettyPrint = true
        }

        return Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .callFactory { okHttpClient.newCall(it) }
            .addCallAdapterFactory(CallAdapterFactory.create())
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRemoteDataSource(retrofit: Retrofit): CountryRemoteDataSource =
        retrofit.create(CountryRemoteDataSource::class.java)
}
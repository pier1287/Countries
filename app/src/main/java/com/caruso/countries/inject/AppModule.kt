package com.caruso.countries.inject

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.caruso.countries.BuildConfig
import com.caruso.countries.repository.CacheDirProvider
import com.caruso.countries.repository.RepositoryConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCacheProvider(@ApplicationContext context: Context): CacheDirProvider =
        object : CacheDirProvider {
            override val cacheDir: File?
                get() = context.cacheDir
        }

    @Provides
    fun provideRepositoryConfig(): RepositoryConfig = RepositoryConfig(BuildConfig.DEBUG)

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .componentRegistry { add(SvgDecoder(context)) }
            .build()
}
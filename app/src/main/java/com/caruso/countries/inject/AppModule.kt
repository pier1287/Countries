package com.caruso.countries.inject

import android.content.Context
import com.caruso.countries.BuildConfig
import com.caruso.countries.repository.CacheDirProvider
import com.caruso.countries.repository.RepositoryConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File

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
}
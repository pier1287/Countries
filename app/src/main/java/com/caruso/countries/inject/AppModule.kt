package com.caruso.countries.inject

import android.content.Context
import com.caruso.countries.repository.CacheDirProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideCacheProvider(@ApplicationContext context: Context): CacheDirProvider =
        object : CacheDirProvider {
            override val cacheDir: File?
                get() = context.cacheDir
        }
}
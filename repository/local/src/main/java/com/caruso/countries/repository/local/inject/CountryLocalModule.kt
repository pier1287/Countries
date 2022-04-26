package com.caruso.countries.repository.local.inject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caruso.countries.repository.local.CountryDao
import com.caruso.countries.repository.local.CountryDetailEntity
import com.caruso.countries.repository.local.CountryEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CountryLocalModule {

    @Provides
    @Singleton
    fun provideCountryDatabase(@ApplicationContext applicationContext: Context): CountryDatabase =
        Room.databaseBuilder(
            applicationContext,
            CountryDatabase::class.java, "country-database"
        ).build()

    @Provides
    @Singleton
    fun provideCountryDao(database: CountryDatabase): CountryDao = database.countryDao()
}

@Database(entities = [CountryEntity::class, CountryDetailEntity::class], version = 1)
internal abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}

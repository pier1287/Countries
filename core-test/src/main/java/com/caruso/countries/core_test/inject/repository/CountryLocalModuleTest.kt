package com.caruso.countries.core_test.inject.repository

import com.caruso.countries.repository.local.CountryDao
import com.caruso.countries.repository.local.inject.CountryLocalModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [CountryLocalModule::class])
object CountryLocalModuleTest {
    @Provides
    @Singleton
    fun provideLocalDataSource(): CountryDao = mockk(relaxed = true) {
        coEvery { getAllCountries() } returns emptyList()
        coEvery { getCountryDetailById(any()) } returns null
    }
}

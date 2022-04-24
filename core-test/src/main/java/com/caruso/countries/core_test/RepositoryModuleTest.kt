package com.caruso.countries.core_test

import com.caruso.countries.repository.CountryRepository
import com.caruso.countries.repository.CountryRepositoryImpl
import com.caruso.countries.repository.inject.RepositoryModule
import com.caruso.countries.repository.remote.CountryDto
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import com.caruso.countries.repository.remote.FlagsDto
import com.caruso.countries.repository.remote.NameDto
import com.caruso.countries.repository.success
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
abstract class RepositoryModuleTest {
    @Binds
    @Singleton
    abstract fun bindCountryRepository(impl: CountryRepositoryImpl): CountryRepository

    companion object {

        private val itaDto = CountryDto(
            "ITA",
            NameDto("ITALY"),
            flags = FlagsDto("ita.png", "svg.png")
        )

        private val fraDto = CountryDto(
            "FRA",
            NameDto("FRANCE"),
            flags = FlagsDto("fra.png", "fra.svg")
        )

        @Provides
        @Singleton
        fun provideRemoteDataSource(): CountryRemoteDataSource = mockk {
            coEvery { getAllCountries() } returns arrayOf(itaDto, fraDto).success()
        }
    }
}

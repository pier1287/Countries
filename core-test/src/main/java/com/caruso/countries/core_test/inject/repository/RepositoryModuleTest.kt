package com.caruso.countries.core_test.inject.repository

import com.caruso.countries.domain.NotFound
import com.caruso.countries.domain.error
import com.caruso.countries.domain.success
import com.caruso.countries.repository.remote.CountryDto
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import com.caruso.countries.repository.remote.FlagsDto
import com.caruso.countries.repository.remote.NameDto
import com.caruso.countries.repository.remote.inject.CountryRemoteModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [CountryRemoteModule::class])
abstract class RemoteDataSourceModuleTest {

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

        private val itaDetailDto = itaDto.copy(
            continents = listOf("EUROPE"),
            capitals = listOf("ROME")
        )

        @Provides
        @Singleton
        fun provideRemoteDataSource(): CountryRemoteDataSource = mockk {
            coEvery { getAllCountries() } returns arrayOf(itaDto, fraDto).success()
            coEvery { getCountryDetailById(any()) } returns NotFound.error()
            coEvery { getCountryDetailById("ITA") } returns arrayOf(itaDetailDto).success()
        }
    }
}

package com.caruso.countries.repository.inject

import com.caruso.countries.repository.CountryRepository
import com.caruso.countries.repository.CountryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindCountryRepository(impl: CountryRepositoryImpl): CountryRepository
}

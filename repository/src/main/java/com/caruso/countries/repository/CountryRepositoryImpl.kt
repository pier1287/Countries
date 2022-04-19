package com.caruso.countries.repository

import kotlinx.coroutines.flow.Flow

internal class CountryRepositoryImpl : CountryRepository {
    override fun observeCountries(): Flow<List<Country>> {
        TODO("Not yet implemented")
    }
}
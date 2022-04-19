package com.caruso.countries.repository

import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun observeCountries(): Flow<ResultOf<List<Country>>>
}
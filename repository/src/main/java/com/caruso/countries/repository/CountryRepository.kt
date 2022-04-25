package com.caruso.countries.repository

import com.caruso.countries.domain.Country
import com.caruso.countries.domain.ResultOf
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun observeCountries(): Flow<ResultOf<List<Country>>>
    fun observeCountryDetail(countryId: String): Flow<ResultOf<Country>>
}
